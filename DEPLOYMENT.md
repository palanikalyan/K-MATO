# K-MATO Deployment Guide

This guide covers deploying the K-MATO food delivery application using Docker Hub and AWS EC2 with Terraform.

## Architecture

```
GitHub → GitHub Actions → Docker Hub → AWS EC2
```

1. **GitHub Actions**: Builds Docker images and pushes to Docker Hub
2. **Docker Hub**: Stores public Docker images
3. **Terraform**: Provisions AWS EC2 infrastructure
4. **EC2 User Data**: Automatically pulls and runs containers on instance startup

## Prerequisites

- Docker installed locally (for local testing)
- AWS CLI configured with credentials
- Terraform installed (v1.0+)
- Docker Hub account
- GitHub repository with Actions enabled

## Part 1: Local Testing with Docker Compose

Test the application locally before deploying to AWS:

```bash
# Build and run containers
docker-compose up --build

# Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8081
```

To stop:
```bash
docker-compose down
```

## Part 2: GitHub Actions Setup

### 1. Create Docker Hub Account
- Go to https://hub.docker.com
- Create an account (free tier is sufficient)
- Note your username

### 2. Add GitHub Secrets
Go to your GitHub repository → Settings → Secrets and variables → Actions

Add these secrets:
- **DOCKER_USERNAME**: Your Docker Hub username
- **DOCKER_PASSWORD**: Your Docker Hub password or access token

### 3. Push to Trigger Build
```bash
git add .
git commit -m "Setup Docker Hub deployment"
git push origin master
```

GitHub Actions will automatically:
- Build backend and frontend Docker images
- Push to Docker Hub as:
  - `<your-username>/kmato-backend:latest`
  - `<your-username>/kmato-frontend:latest`

Check the progress: GitHub → Actions tab

## Part 3: AWS Deployment with Terraform

### 1. Configure AWS Credentials
```powershell
# Set AWS credentials as environment variables
$env:AWS_ACCESS_KEY_ID="your-access-key"
$env:AWS_SECRET_ACCESS_KEY="your-secret-key"
$env:AWS_DEFAULT_REGION="us-east-1"
```

Or use AWS CLI:
```bash
aws configure
```

### 2. Update Terraform Variables
Edit `infra/terraform/terraform.tfvars` or create it:

```hcl
docker_username = "your-dockerhub-username"
aws_region      = "us-east-1"
instance_type   = "t3.micro"

# Optional: Override defaults
jwt_secret             = "your-custom-jwt-secret-min-256-bits"
datasource_url         = "jdbc:h2:file:./data/fooddelivery;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
datasource_username    = "sa"
datasource_password    = ""
```

**IMPORTANT**: Replace `your-dockerhub-username` with your actual Docker Hub username!

### 3. Initialize Terraform
```bash
cd infra/terraform
terraform init
```

### 4. Plan Infrastructure
```bash
terraform plan
```

Review the planned resources:
- Security Group (ports 80, 8081, 22)
- IAM Role and Instance Profile
- EC2 Instance (t3.micro with Amazon Linux 2)

### 5. Apply Infrastructure
```bash
terraform apply
```

Type `yes` when prompted.

Terraform will:
1. Create AWS resources (Security Group, IAM, EC2)
2. EC2 user-data script will:
   - Install Docker and Docker Compose
   - Pull images from Docker Hub
   - Start backend on port 8081
   - Start frontend on port 80

This takes 3-5 minutes.

### 6. Get Application URLs
```bash
terraform output
```

Example output:
```
backend_url = "http://54.123.45.67:8081"
frontend_url = "http://54.123.45.67"
ec2_public_ip = "54.123.45.67"
```

### 7. Access Your Application
- **Frontend**: Open browser to `http://<ec2_public_ip>`
- **Backend API**: `http://<ec2_public_ip>:8081`

Wait 2-3 minutes after Terraform completes for Docker containers to fully start.

## Part 4: Verify Deployment

### Check Container Status via SSH
```bash
# Get EC2 public IP from Terraform output
ssh -i your-key.pem ec2-user@<ec2_public_ip>

# Check running containers
docker ps

# View backend logs
docker logs kmato-backend

# View frontend logs
docker logs kmato-frontend

# Check startup log
cat /var/log/kmato-startup.log
```

### Health Checks
```bash
# Backend health (from local machine)
curl http://<ec2_public_ip>:8081/actuator/health

# Frontend (open in browser)
http://<ec2_public_ip>
```

## Part 5: Updates and Redeployment

### Update Application Code
1. Make code changes
2. Commit and push to GitHub
   ```bash
   git add .
   git commit -m "Update feature"
   git push origin master
   ```
3. GitHub Actions builds and pushes new images
4. SSH to EC2 and restart containers:
   ```bash
   ssh -i your-key.pem ec2-user@<ec2_public_ip>
   
   # Pull latest images
   docker pull <your-username>/kmato-backend:latest
   docker pull <your-username>/kmato-frontend:latest
   
   # Restart containers
   docker restart kmato-backend
   docker restart kmato-frontend
   ```

### Update Infrastructure
```bash
cd infra/terraform
terraform plan
terraform apply
```

## Part 6: Cleanup

To destroy all AWS resources:
```bash
cd infra/terraform
terraform destroy
```

Type `yes` to confirm.

## Troubleshooting

### GitHub Actions Build Fails
- Check DOCKER_USERNAME and DOCKER_PASSWORD secrets are set
- Verify Dockerfiles exist in correct paths

### EC2 Containers Not Starting
```bash
# SSH to EC2
ssh -i your-key.pem ec2-user@<ec2_public_ip>

# Check Docker service
sudo systemctl status docker

# Check container logs
docker logs kmato-backend
docker logs kmato-frontend

# Manually pull and run
docker pull <your-username>/kmato-backend:latest
docker run -d --name kmato-backend -p 8081:8081 <your-username>/kmato-backend:latest
```

### Cannot Access Application
1. Check Security Group allows ports 80, 8081, 22
2. Verify EC2 has public IP: `terraform output ec2_public_ip`
3. Wait 3-5 minutes for containers to fully start
4. Check container status: `docker ps`

### Terraform Apply Fails
- Verify AWS credentials are configured
- Check AWS region has available resources
- Ensure docker_username variable is set

## Cost Estimation

- **EC2 t3.micro**: ~$0.0104/hour (~$7.50/month in us-east-1)
- **Docker Hub**: Free for public repositories
- **GitHub Actions**: 2,000 minutes/month free
- **Data Transfer**: Minimal cost for normal usage

## Security Notes

- EC2 Security Group restricts access to ports 80, 8081, 22 only
- Docker Hub images are public - do not include secrets in code
- Use environment variables for sensitive configuration
- Consider using HTTPS with a domain and SSL certificate for production
- Restrict SSH access (port 22) to your IP in Security Group

## Production Recommendations

1. **Domain & SSL**: Use Route 53 + ACM certificate
2. **Database**: Replace H2 with RDS (PostgreSQL/MySQL)
3. **Load Balancer**: Add ALB for high availability
4. **Auto Scaling**: Use Auto Scaling Group
5. **Monitoring**: Enable CloudWatch logs and metrics
6. **Secrets**: Use AWS Secrets Manager for sensitive data
7. **Private Registry**: Use Docker Hub private repos or AWS ECR
8. **CI/CD**: Add automated tests before deployment

## Support

For issues:
1. Check logs: `docker logs kmato-backend` and `docker logs kmato-frontend`
2. Review GitHub Actions workflow runs
3. Verify Terraform state: `terraform show`
