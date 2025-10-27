# K-MATO Deployment Checklist

## Pre-Deployment Setup

### Docker Hub
- [ ] Create Docker Hub account at https://hub.docker.com
- [ ] Note your Docker Hub username: `_________________`

### GitHub Secrets
- [ ] Go to GitHub repo → Settings → Secrets and variables → Actions
- [ ] Add secret: `DOCKER_USERNAME` = your Docker Hub username
- [ ] Add secret: `DOCKER_PASSWORD` = your Docker Hub password/token

### AWS Credentials
- [ ] AWS Access Key ID configured
- [ ] AWS Secret Access Key configured
- [ ] AWS CLI installed and configured (`aws configure`)

### Terraform
- [ ] Terraform installed (version 1.0+)
- [ ] Copy `infra/terraform/terraform.tfvars.example` to `infra/terraform/terraform.tfvars`
- [ ] Update `docker_username` in terraform.tfvars
- [ ] (Optional) Update other variables (jwt_secret, region, instance_type)

## Deployment Steps

### Step 1: Test Locally
```bash
docker-compose up --build
```
- [ ] Backend accessible at http://localhost:8081
- [ ] Frontend accessible at http://localhost
- [ ] Stop containers: `docker-compose down`

### Step 2: Build and Push to Docker Hub
```bash
git add .
git commit -m "Ready for deployment"
git push origin master
```
- [ ] Check GitHub Actions → Actions tab
- [ ] Wait for "Build and Push Docker Images" workflow to complete (green checkmark)
- [ ] Verify images on Docker Hub: `<username>/kmato-backend:latest` and `<username>/kmato-frontend:latest`

### Step 3: Deploy to AWS
```bash
cd infra/terraform
terraform init
terraform plan
terraform apply
```
- [ ] Review plan (Security Group, IAM, EC2)
- [ ] Type `yes` to apply
- [ ] Wait 3-5 minutes for completion
- [ ] Note the outputs (ec2_public_ip, backend_url, frontend_url)

### Step 4: Verify Deployment
- [ ] Wait 2-3 minutes for EC2 user-data script to complete
- [ ] Open frontend URL in browser: `http://<ec2_public_ip>`
- [ ] Test backend health: `curl http://<ec2_public_ip>:8081/actuator/health`
- [ ] (Optional) SSH to EC2 and check containers: `docker ps`

## Post-Deployment

### Verify Functionality
- [ ] Register a new user account
- [ ] Login with the account
- [ ] Create a restaurant (admin approval workflow)
- [ ] Add menu items
- [ ] Place an order
- [ ] Check restaurant owner orders tab

### Document URLs
- Frontend: `http://_________________`
- Backend: `http://_________________:8081`
- EC2 Public IP: `_________________`

## Cleanup (When Done Testing)
```bash
cd infra/terraform
terraform destroy
```
- [ ] Type `yes` to confirm
- [ ] Verify all resources deleted in AWS Console

## Troubleshooting

If frontend/backend not accessible:
1. Check EC2 Security Group allows ports 80, 8081, 22
2. SSH to EC2: `ssh -i your-key.pem ec2-user@<ec2_public_ip>`
3. Check containers: `docker ps`
4. Check logs: `docker logs kmato-backend` and `docker logs kmato-frontend`
5. Check startup log: `cat /var/log/kmato-startup.log`

If GitHub Actions fails:
1. Verify DOCKER_USERNAME and DOCKER_PASSWORD secrets are set
2. Check workflow logs in Actions tab
3. Verify Dockerfile paths are correct

## Costs
- EC2 t3.micro: ~$7.50/month
- Docker Hub: Free (public repos)
- GitHub Actions: Free (2,000 minutes/month)

## Next Steps (Optional Production Enhancements)
- [ ] Add custom domain with Route 53
- [ ] Configure SSL certificate with ACM
- [ ] Replace H2 with RDS database
- [ ] Add Application Load Balancer
- [ ] Set up Auto Scaling Group
- [ ] Enable CloudWatch monitoring
- [ ] Use AWS Secrets Manager for sensitive data
