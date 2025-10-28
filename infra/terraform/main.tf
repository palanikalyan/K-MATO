terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

resource "aws_security_group" "kmato_sg" {
  name        = "kmato-sg"
  description = "Allow HTTP and SSH access"

  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "App Port"
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

data "aws_ami" "amazon_linux2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

resource "aws_iam_role" "ec2_role" {
  name = "kmato-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ssm_core" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "kmato-ec2-profile"
  role = aws_iam_role.ec2_role.name
}

resource "aws_instance" "kmato" {
  ami                         = data.aws_ami.amazon_linux2.id
  instance_type               = var.instance_type
  vpc_security_group_ids      = [aws_security_group.kmato_sg.id]
  iam_instance_profile        = aws_iam_instance_profile.ec2_profile.name
  associate_public_ip_address = true

  user_data = <<-EOF
              #!/bin/bash
              exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1
              set -x
              
              echo "Starting deployment at $(date)"
              
              # Update and install Docker
              yum update -y
              amazon-linux-extras install docker -y
              service docker start
              usermod -a -G docker ec2-user
              systemctl enable docker
              
              # Wait for Docker to be ready
              echo "Waiting for Docker to be ready..."
              sleep 15
              
              # Pull and run backend container from Docker Hub
              echo "Pulling backend image at $(date)"
              docker pull ${var.docker_username}/kmato-backend:latest
              docker rm -f kmato-backend || true
              echo "Starting backend container at $(date)"
              docker run -d \
                --name kmato-backend \
                --restart unless-stopped \
                -p 8081:8081 \
                -e JWT_SECRET="${var.jwt_secret}" \
                -e SPRING_DATASOURCE_URL="${var.datasource_url}" \
                -e SPRING_DATASOURCE_USERNAME="${var.datasource_username}" \
                -e SPRING_DATASOURCE_PASSWORD="${var.datasource_password}" \
                ${var.docker_username}/kmato-backend:latest
              
              # Pull and run frontend container from Docker Hub
              echo "Pulling frontend image at $(date)"
              docker pull ${var.docker_username}/kmato-frontend:latest
              docker rm -f kmato-frontend || true
              echo "Starting frontend container at $(date)"
              docker run -d \
                --name kmato-frontend \
                --restart unless-stopped \
                -p 80:80 \
                ${var.docker_username}/kmato-frontend:latest
              
              # Log container status
              echo "Deployment completed at $(date)"
              echo "Container status:"
              docker ps -a
              
              echo "Backend logs:"
              docker logs kmato-backend --tail 50
              
              echo "Frontend logs:"
              docker logs kmato-frontend --tail 50
              EOF

  tags = {
    Name = "kmato-server"
  }
}

output "ec2_public_ip" {
  value       = aws_instance.kmato.public_ip
  description = "Public IP of the EC2 instance"
}

output "backend_url" {
  value       = "http://${aws_instance.kmato.public_ip}:8081"
  description = "Backend API URL"
}

output "frontend_url" {
  value       = "http://${aws_instance.kmato.public_ip}"
  description = "Frontend application URL"
}
