terraform {
  # using the >= version constraint syntax declares the minimum provider version.
  required_version = ">= 1.1.3"
  required_providers {
    aws = {
      source = "hashicorp/aws"
      # The ~> operator specify the maximum provider version it is intended to work with.
      version = "= 3.72"
    }
  }
}
