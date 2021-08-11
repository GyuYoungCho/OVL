import AWS from "aws-sdk";

export default {
    upload: async (fileList, type) => {
    const albumBucketName = 'ovl-bucket'; // S3의 버킷 이름
    const region = 'ap-northeast-2'; // 서울
    const accessKeyId = 'AKIA36BVZDJBOE5XZDOC'; // IAM에서 생성한 사용자의 accessKeyId
    const secretAccessKey = 'Yn8hl1C8mxlT+cdQrw80PCF8ZbpM0mqq9JUCf6yL'; // IAM에서 생성한 사용자의 secretAccessKey

    AWS.config.update({
      region,
      accessKeyId,
      secretAccessKey
    }); 
    
    var s3 = new AWS.S3({
        apiVersion:'2012-10-17',
        params: {Bucket: albumBucketName,}
    })
    
    var photoKeyList = ["temp"];

    var promises = [];
      
    fileList.forEach(async file => {
        var fileName = file.name;
        var types = type + '/';

        var slice = fileName.split(".");

        var photoKey = types + slice[0] + "_" + new Date().getTime() + "." + slice[1];
         
        const result = s3.upload({
            Key: photoKey,
            Body: file,
            ACL: 'public-read'
        }).promise().then(function (data) {
            photoKeyList.push(data.Location);
        }).catch(function (err) {
            console.log(err);
        });

        await promises.push(result);

      })
      
      await Promise.all(promises);
      
      return photoKeyList;

  }
}