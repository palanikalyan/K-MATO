# K-MATO Quick Start Guide

## 🚀 Deploy to AWS in 5 Steps

### 1️⃣ Setup Docker Hub (2 minutes)
- Create account at https://hub.docker.com
- Note your username

### 2️⃣ Configure GitHub Secrets (2 minutes)
Go to: **GitHub repo → Settings → Secrets → Actions**

Add:
- `DOCKER_USERNAME` = your Docker Hub username
- `DOCKER_PASSWORD` = your Docker Hub password

### 3️⃣ Configure Terraform (2 minutes)
```bash
cd infra/terraform
cp terraform.tfvars.example terraform.tfvars
```

Edit `terraform.tfvars`:
```hcl
docker_username = "YOUR-DOCKERHUB-USERNAME"  # ⚠️ CHANGE THIS!
```

### 4️⃣ Trigger Build (1 minute)
```bash
git add .
git commit -m "Deploy to AWS"
git push origin master
```

Wait for GitHub Actions to build images (check Actions tab).

### 5️⃣ Deploy Infrastructure (5 minutes)
```bash
# Set AWS credentials
$env:AWS_ACCESS_KEY_ID="your-key"
$env:AWS_SECRET_ACCESS_KEY="your-secret"

# Deploy
cd infra/terraform
terraform init
terraform apply
```

Type `yes` when prompted.

## ✅ Access Your App

After deployment completes:
```bash
terraform output
```

Open in browser:
- **Frontend**: `http://<ec2_public_ip>`
- **Backend**: `http://<ec2_public_ip>:8081`

Wait 2-3 minutes for containers to start.

## 🧪 Test Locally First (Optional)

```bash
docker-compose up --build
```

- Frontend: http://localhost
- Backend: http://localhost:8081

Stop: `docker-compose down`

## 🔧 Troubleshooting

**App not loading?**
```bash
ssh ec2-user@<ec2_public_ip>
docker ps
docker logs kmato-backend
docker logs kmato-frontend
```

**GitHub Actions failed?**
- Check DOCKER_USERNAME and DOCKER_PASSWORD secrets are set correctly
- View logs in GitHub → Actions tab

**Terraform apply failed?**
- Verify AWS credentials: `aws sts get-caller-identity`
- Check docker_username is set in terraform.tfvars

## 🗑️ Cleanup

When done testing:
```bash
cd infra/terraform
terraform destroy
```

Type `yes` to remove all AWS resources.

## 📚 Full Documentation

- **Complete Guide**: See [DEPLOYMENT.md](DEPLOYMENT.md)
- **Checklist**: See [DEPLOYMENT-CHECKLIST.md](DEPLOYMENT-CHECKLIST.md)

## 💰 Cost

- **Free Tier**: EC2 t3.micro eligible for AWS free tier (750 hours/month)
- **After Free Tier**: ~$7.50/month for t3.micro
- **Docker Hub**: Free for public repos
- **GitHub Actions**: Free (2,000 minutes/month)

## 🎯 What Gets Deployed

```
GitHub Actions
    ↓
Docker Hub (Images)
    ↓
AWS EC2 (t3.micro)
    ├── Security Group (ports 80, 8081, 22)
    ├── IAM Role (SSM access)
    ├── Backend Container (Spring Boot)
    └── Frontend Container (Angular + Nginx)
```

## 🔐 Security Notes

- EC2 Security Group restricts access to ports 80, 8081, 22 only
- Use environment variables for secrets (JWT_SECRET)
- Docker Hub images are public - don't commit secrets
- For production: Use private Docker repos, SSL, and RDS database

## ❓ Common Questions

**Q: Can I use a different AWS region?**  
A: Yes, edit `aws_region = "us-east-1"` in terraform.tfvars

**Q: How do I update my application?**  
A: Push changes to GitHub → Actions builds new images → SSH to EC2 → `docker restart kmato-backend kmato-frontend`

**Q: Can I use a custom domain?**  
A: Yes, configure Route 53 and add an SSL certificate. See DEPLOYMENT.md production section.

**Q: How do I access logs?**  
A: SSH to EC2 → `docker logs kmato-backend` or `docker logs kmato-frontend`

## 📞 Support

Check logs and documentation first:
1. Application logs: `docker logs <container-name>`
2. Startup log: `cat /var/log/kmato-startup.log`
3. Full guide: [DEPLOYMENT.md](DEPLOYMENT.md)
