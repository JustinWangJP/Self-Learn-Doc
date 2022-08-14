###################################
# Common variables
###################################

variable "profile" {
  type    = string
  default = "default"
}

variable "region-master" {
  type    = string
  default = "us-east-1"

}

variable "region-worker" {
  type    = string
  default = "us-west-2"

}

variable "external_ip" {
  type    = string
  default = "106.72.144.194/32"

}

