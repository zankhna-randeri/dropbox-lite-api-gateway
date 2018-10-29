# Dropbox Lite - Backend API

Project 1 : Dropbox-Lite  
University Name: http://www.sjsu.edu/  
Course: Cloud Technologies  
Professor: [Sanjay Garje](https://www.linkedin.com/in/sanjaygarje/)  
ISA: [Anushri Srinath Aithal](https://www.linkedin.com/in/anushri-aithal/)  
Student: [Zankhna Randeri](https://www.linkedin.com/in/zankhna-randeri/)  

### Application URL
http://zankhna-randeri.site

### Demo link  
[Click here for video](https://drive.google.com/open?id=1VcfirhI-T8-Lg-e8QjoeVo42b7fbHK1n)

### Introduction

Dropbox-lite is a highly available, highly scalable and cost-effective web-based 3-tier application that is hosted on AWS cloud.It provides secure file management to authorized users. The application uses various AWS cloud technologies  such as S3 and RDS to store data related to files and users. The application’s api-backend and front-end are deployed on EC2 instances.  The api-backend can serve both Web application as well as Mobile application. It also implements auto scaling functionality to provide better user experience during peak times. Dropbox-lite provides basic functionalities as follows:  
1) User can register on the website.  
2) Registered user can upload file to his/her account.  
3) User can delete file from account.  
4) User can update uploaded file.  
5) Admin user can view files uploaded by all users.  
6) Admin user can delete file uploaded by any user.  

### Application Features  
1) Dropbox-Lite allows new user to register on website. Already registered user can login to his/her account. Spring Security is implemented that provides site-wide user authorization and prevents direct access to user Profile page.  
2) The user account will display basic user information along with all the files uploaded by that particular user.  
3) User can upload new files to store them on Dropbox-Lite cloud drive. Maximum allowed file size is 10MB.  
4) User can delete file from his/her drive.  
5) User can update already uploaded file.  
6) User can securely download file from account.  
7) Admin user will have ability to view all the files uploaded by all users of Dropbox-Lite.  
8) Admin user will have ability to delete any file uploaded by Dropbox-Lite user

### AWS components integration  
1) Route53: Drpobox-Lite application is hosted on Route53. Route53 will route user request to Network Load Balancer.  
2) Network Load Balancer : Network Load Balancer will handle incoming traffic to target EC2 instances.  
3) EC2: Dropbox-Lite application api-backend and front-end are deployed on EC2 machines with auto scaling configuration.  
4) Autoscaling Group: Autoscaling Group associated with EC2 machine will provide availability and scalability during peak hours.  
5) S3: S3(Standard) will store user files on cloud. Following configurations are done:  
  a) Cross Region Replication (CRR) : to provide high availability of data in case if one region goes down.  
  b) Transfer Acceleration: to  provide faster file upload.  
6) Standard IA: S3 lifecycle policy will move file to Standard-IA after 75 days.  
7) Amazon Glacier: S3 lifecycle policy will move file to Amazon Glacier after 365 days.  
8) CloudFront: CloudFront will provide secure file download. Only authorized user with pre-signed url can download file from cloudfront. Presigned url is valid only for 2 minute after its creation.  
9) RDS: MySQL instance on RDS will store user and file metadata of DropBox-Lite web application. RDS is enabled with multi-AZ configuration to provide high availability.  
10) Cloudwatch:  
  a) Cloudwatch alarm are set with elastic load balancer. Whenever users start getting “Http 500” error on website, ELB will send an alarm to cloud watch. Cloudwatch alarm will send notification notification via SNS.  
  b) Cloudwatch is also configured with Autoscaling group. Whenever new instance is launched or terminated, it will trigger a Lambda and lambda will send a notification through SNS.  
11) Lambda: A python program configured to trigger SNS topic.  
12) SNS: It will send an email to all subscriber related to instance health check status.  
13) Continuous Integration/Continuous Deployment : (CI/CD)  
  a) Code Build: AWS Code Build will be integrated with github repository and it will automatically build code according to specified build specs. Whenever new code is pushed to Git-repo, code-build will automatically build that code.  
  b) Code Deploy: Code deploy will have automatically deploy code on specified on-premise EC2 or Autoscaling group. if there is no new instance available then It will automatically launch new instance to application deployment.  
  c) Code Pipeline: It will allow to create source-build-deploy pipeline for different stages, such as Development and deployment. It will allow build stage control by disabling stage transition.  

### AWS architect of project  

![cluoud-project](https://user-images.githubusercontent.com/42704669/47683787-e3971080-db8d-11e8-9756-e9dad5667a6a.png)  

### Screenshots  

SignIn screen  
![screen shot 2018-10-29 at 12 21 01 pm](https://user-images.githubusercontent.com/42704669/47683860-23f68e80-db8e-11e8-8628-a0c2ee2f7234.png)

Registration Screen  
![screen shot 2018-10-29 at 12 21 13 pm](https://user-images.githubusercontent.com/42704669/47683867-28bb4280-db8e-11e8-9031-21b41e68b8c8.png)

User screen  
![screen shot 2018-10-29 at 12 20 52 pm](https://user-images.githubusercontent.com/42704669/47683857-1e994400-db8e-11e8-831a-60573a0f1bc4.png)

File Delete  
![screen shot 2018-10-29 at 12 26 50 pm](https://user-images.githubusercontent.com/42704669/47683876-3244aa80-db8e-11e8-8cb3-1344b39c3384.png)  
