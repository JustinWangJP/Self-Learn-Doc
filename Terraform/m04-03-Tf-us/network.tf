
###############################################################################################
# VPC
# README: https://registry.terraform.io/modules/terraform-aws-modules/vpc/aws/latest
# Source: https://github.com/terraform-aws-modules/terraform-aws-vpc/
###############################################################################################

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.11.3"

  # VPC Definitions
  cidr                 = "10.1.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  # Subnet Definitions
  azs              = ["${var.region}a", "${var.region}c"]
  public_subnets   = ["10.1.0.0/24", "10.1.10.0/24"]
  private_subnets  = ["10.1.1.0/24", "10.1.11.0/24"]
  database_subnets = ["10.1.2.0/24", "10.1.12.0/24"]

  create_database_subnet_group = false

  # Route Table Definitions
  create_database_subnet_route_table = true

  # NAT Gateway Definitions
  enable_nat_gateway = true
  single_nat_gateway = true

  # VPN Gateway Definitions
  enable_vpn_gateway = false

  # VPC Flow Logs Definitions
  enable_flow_log                                 = true
  create_flow_log_cloudwatch_log_group            = true
  create_flow_log_cloudwatch_iam_role             = true
  flow_log_cloudwatch_log_group_name_prefix       = "${var.name}-loggroup-flowlogs-"
  flow_log_max_aggregation_interval               = 60
  flow_log_cloudwatch_log_group_retention_in_days = 30

  # Resource Naming and Tagging Definitions
  vpc_tags = {
    Name = "${var.name}-VPC"
  }

  public_subnet_tags = {
    Name   = "${var.name}-Subnet-Public"
    Public = "Yes"
  }

  private_subnet_tags = {
    Name = "${var.name}-Subnet-Private-AP"
  }

  database_subnet_tags = {
    Name = "${var.name}-Subnet-Private-DB"
  }

  public_route_table_tags = {
    Name = "${var.name}-Route-Public"
  }

  private_route_table_tags = {
    Name = "${var.name}-Route-Private-AP"
  }

  database_route_table_tags = {
    Name = "${var.name}-Route-Private-DB"
  }

  igw_tags = {
    Name = "${var.name}-IGW"
  }

  nat_eip_tags = {
    Name = "${var.name}-EIP-NATGW"
  }

  nat_gateway_tags = {
    Name = "${var.name}-NATGW"
  }

  vpc_flow_log_tags = {
    Name = "${var.name}-VPC-FlowLogs"
  }

  tags = var.tags

}

