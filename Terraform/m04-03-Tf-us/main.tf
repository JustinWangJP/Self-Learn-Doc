###############################################################################################
# Security Group
###############################################################################################
###################################
# Public EC2 with egress limited
###################################
resource "aws_security_group" "sg-ec2-public" {
  name        = "${var.name}-SG-ec2-public"
  description = "EC2 on Public subnet"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_own_public_ip]
  }

  ingress {
    from_port   = -1
    to_port     = -1
    protocol    = "icmp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }

  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = -1
    to_port     = -1
    protocol    = "icmp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }
  egress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }
  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }

  tags = var.tags
}

###################################
# Private EC2 with egress limited
###################################
resource "aws_security_group" "sg-ec2-private" {
  name        = "${var.name}-SG-ec2-private"
  description = "EC2 on Private subnet"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = -1
    to_port     = -1
    protocol    = "icmp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }

  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = -1
    to_port     = -1
    protocol    = "icmp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }

  tags = var.tags
}

###############################################################################################
# EC2 instance
###############################################################################################
# Get tha latest Amazon Linux2 AMI
data "aws_ami" "amazon_linux2" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}

###################################
# Public EC2(Bastion) 
###################################
resource "aws_instance" "ec2_instance_bastion" {
  ami                  = data.aws_ami.amazon_linux2.id
  instance_type        = "t2.micro"
  iam_instance_profile = aws_iam_instance_profile.systems_manager.name
  availability_zone    = element(module.vpc.azs, 0)
  subnet_id            = element(module.vpc.public_subnets, 0)
  root_block_device {
    volume_type = "gp3"
    volume_size = 8
    encrypted   = "true"
    tags        = var.tags
  }
  vpc_security_group_ids = [aws_security_group.sg-ec2-public.id]
  key_name               = var.key_name

  tags = merge(
    var.tags,
    {
      Name   = "${var.name}-EC2-bastion"
      Public = "Yes"
    },
  )
  # UserData
  user_data = <<EOF
#!/bin/bash
yum update -y
EOF
}

###################################
# Private EC2(App) 
###################################
resource "aws_instance" "ec2_instance_app" {
  ami                  = data.aws_ami.amazon_linux2.id
  instance_type        = "t2.micro"
  iam_instance_profile = aws_iam_instance_profile.systems_manager.name
  availability_zone    = element(module.vpc.azs, 0)
  subnet_id            = element(module.vpc.private_subnets, 0)
  root_block_device {
    volume_type = "gp3"
    volume_size = 8
    encrypted   = "true"
    tags        = var.tags
  }
  vpc_security_group_ids = [aws_security_group.sg-ec2-private.id]
  key_name               = var.key_name

  tags = merge(
    var.tags,
    {
      Name = "${var.name}-EC2-app"
    },
  )

  # UserData
  user_data = <<EOF
#!/bin/bash
yum update -y
EOF
}

