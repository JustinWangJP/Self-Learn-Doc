###############################################################################################
# IAM
###############################################################################################
###################################
# IAM Role for SSM
###################################
data "aws_iam_policy_document" "assume_role" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "role-ec2-ssm" {
  name               = "${var.name}-Role-ec2-ssm"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json

  tags = var.tags
}

data "aws_iam_policy" "systems_manager" {
  arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_role_policy_attachment" "default" {
  role       = aws_iam_role.role-ec2-ssm.name
  policy_arn = data.aws_iam_policy.systems_manager.arn
}

resource "aws_iam_instance_profile" "systems_manager" {
  name = "${var.name}-Instance-Profile-ssm"
  role = aws_iam_role.role-ec2-ssm.name
}

