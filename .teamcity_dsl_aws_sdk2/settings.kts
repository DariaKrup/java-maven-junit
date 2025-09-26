import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.07"

project {

    features {
        awsConnection {
            id = "AWS_AmazonWebServicesAws1"
            name = "Amazon Web Services (AWS) (local)"
            regionName = "eu-west-1"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVPNMDV7CM"
                secretAccessKey = "credentialsJSON:d70c05ef-b68d-4fce-a9be-5ea0c4b87cc9"
            }
            allowInSubProjects = true
            allowInBuilds = true
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        awsConnection {
            id = "AWS_AmazonWebServicesAwsIamRole"
            name = "Amazon Web Services (AWS) (IAM Role)"
            regionName = "eu-west-1"
            credentialsType = iamRole {
                roleArn = "arn:aws:iam::913206223978:role/dkrupkina-eks-cluster"
                awsConnectionId = "AmazonWebServicesAws_2"
            }
            allowInBuilds = false
            stsEndpoint = "https://sts.eu-west-1.amazonaws.com"
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_553"
            profileId = "amazon-50"
            imagePriority = 4
            name = "Ubuntu"
            vpcSubnetId = "subnet-0c4f70b91d8800740,subnet-0ee5ddb298ed3d189"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudImage {
            id = "PROJECT_EXT_554"
            profileId = "amazon-53"
            agentPoolId = "-2"
            name = "AWS Ubuntu"
            vpcSubnetId = "subnet-0ee5ddb298ed3d189"
            iamProfile = "dkrupkina-eks-nodes"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudProfile {
            id = "amazon-50"
            name = "AWS EC2"
            serverURL = "http://10.128.93.57:8281"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AWS_AmazonWebServicesAws1"
            maxInstancesCount = 1
        }
        amazonEC2CloudProfile {
            id = "amazon-52"
            name = "AWS Role"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AWS_AmazonWebServicesAwsIamRole"
        }
        amazonEC2CloudProfile {
            id = "amazon-53"
            name = "AWS Role (correct)"
            serverURL = "http://10.128.93.57:8281"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            awsConnectionId = "AWS_AmazonWebServicesAwsCorrectRole"
        }
    }

    subProject(SubProject)
}


object SubProject : Project({
    name = "SubProject"

    features {
        amazonEC2CloudProfile {
            id = "amazon-51"
            name = "EC2"
            serverURL = "http://10.128.93.57:8281"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.US_EAST_N_VIRGINIA
            awsConnectionId = "AWS_AmazonWebServicesAws1"
        }
    }
})
