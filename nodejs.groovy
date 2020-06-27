job('NodeJS-dev-api') {
    logRotator {
         numToKeep 5
        }
    scm {
        git('https://sharmamukesh49@bitbucket.org/browndeckventuresllc/ajna-api.git')
        credentialsID('Source-Access')
        branch('*/CDEV')  
            }
    wrappers {
        nodejs('default') // this is the name of the NodeJS installation in 
                         // Manage Jenkins -> Configure Tools -> NodeJS Installations -> Name
    }
    steps {
        shell("npm install")
        shell("aws s3 cp s3://ajnadeployment/cdev/deploy/  .   --recursive")
        shell("aws s3 cp s3://ajnadeployment/cdev/backend_config/cdev.json  $WORKSPACE/config/")
        shell("zip -r $JOB_NAME-1.2.$BUILD_ID.zip * .[^.]*")
        shell("aws s3 cp $JOB_NAME-1.2.$BUILD_ID.zip s3://ajnabuildartifact")
        shell("aws deploy create-deployment	--region us-east-1 --application-name ajna-cdev --deployment-config-name CodeDeployDefault.AllAtOnce --deployment-group-name ajna-cdev-api --description Ajna dev Api deployment --s3-location bucket=ajnabuildartifact,bundleType=zip,key=$JOB_NAME-1.2.$BUILD_ID.zip")
    }
}
