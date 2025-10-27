variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "docker_username" {
  type        = string
  description = "Docker Hub username for pulling images"
}

variable "jwt_secret" {
  type        = string
  description = "JWT secret for backend authentication"
  default     = "your-secret-key-min-256-bits-change-this-in-production"
  sensitive   = true
}

variable "datasource_url" {
  type        = string
  description = "Database connection URL"
  default     = "jdbc:h2:file:./data/fooddelivery;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
}

variable "datasource_username" {
  type        = string
  description = "Database username"
  default     = "sa"
}

variable "datasource_password" {
  type        = string
  description = "Database password"
  default     = ""
  sensitive   = true
}
