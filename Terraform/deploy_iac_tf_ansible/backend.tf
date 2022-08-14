terraform {
  required_version = ">= 1.1.3"
  backend "s3" {
    region  = "us-east-1"
    profile = "default"
    key     = "terraformstatefile"
    bucket  = "tterraformsatebucket"
  }
}