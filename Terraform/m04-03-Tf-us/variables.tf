###################################
# Common variables
###################################

variable "name" {
  description = "Project name"
  type        = string
  default     = "CRT00-Tf-Demo"
}

variable "region" {
  description = "AWS Region"
  type        = string
  default     = "us-east-1"
}

variable "tags" {
  description = "A map of tags to all AWS Resources"
  type        = map(string)
  default = {
    System      = "ProjectCRT"
    Environment = "Development"
  }
}

###################################
# EC2 variables
###################################
variable "key_name" {
  type        = string
  description = "Enter the name of your AWS key pair name you've already created"
}


###################################
# Security Group variables
###################################
variable "my_own_public_ip" {
  type        = string
  description = "Enter your work location's global ip address to restrict ssh access source"
}
