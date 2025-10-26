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

resource "aws_ecr_repository" "backend" {
  name = "kmato-backend"
}

resource "aws_ecr_repository" "frontend" {
  name = "kmato-frontend"
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

resource "aws_iam_role_policy_attachment" "ecr_readonly" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
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
  ami                    = data.aws_ami.amazon_linux2.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.kmato_sg.id]
  iam_instance_profile   = aws_iam_instance_profile.ec2_profile.name
  associate_public_ip_address = true

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              amazon-linux-extras install docker -y
              service docker start
              usermod -a -G docker ec2-user
              yum install -y aws-cli

              REGION=${var.aws_region}
              BACKEND_REPO=${aws_ecr_repository.backend.repository_url}
              FRONTEND_REPO=${aws_ecr_repository.frontend.repository_url}

              # wait for images to be pushed to ECR and then run them
              for i in {1..60}; do
                aws ecr get-login-password --region "$${REGION}" | docker login --username AWS --password-stdin ${aws_ecr_repository.backend.repository_url%/*}
                if docker pull ${aws_ecr_repository.backend.repository_url}:latest; then
                  break
                fi
                sleep 10
              done

              docker rm -f kmato-backend || true
              docker run -d --name kmato-backend -p 8081:8081 ${aws_ecr_repository.backend.repository_url}:latest || true

              for i in {1..60}; do
                aws ecr get-login-password --region "$${REGION}" | docker login --username AWS --password-stdin ${aws_ecr_repository.frontend.repository_url%/*}
                if docker pull ${aws_ecr_repository.frontend.repository_url}:latest; then
                  break
                fi
                sleep 10
              done

              docker rm -f kmato-frontend || true
              docker run -d --name kmato-frontend -p 80:80 ${aws_ecr_repository.frontend.repository_url}:latest || true
              EOF

  tags = {
    Name = "kmato-server"
  }
}

output "ecr_backend_url" {
  value = aws_ecr_repository.backend.repository_url
}

output "ecr_frontend_url" {
  value = aws_ecr_repository.frontend.repository_url
}

output "ec2_public_ip" {
  value = aws_instance.kmato.public_ip
}
