package chistosito

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import aws.smithy.kotlin.runtime.content.writeToFile
import chistosito.util.FIleUtils
import java.io.File

class AWSWorker {

    companion object {
        suspend fun s3ObjectDownload(keyName: String, bucketName: String, dest: String) : String {
            if(FIleUtils.fileExists(dest))
                return dest
            val request =  GetObjectRequest {
                key = keyName
                bucket= bucketName
            }

            S3Client { region = "eu-central-1" }.use { s3 ->
                s3.getObject(request) { resp ->
                    val myFile = File(dest)
                    resp.body?.writeToFile(myFile)
                    println("Successfully read $keyName from $bucketName")
                }
            }
            return dest
        }

        suspend fun listObjectsInBucket(path: String, bucketName: String, filter: String?): List<String>{
            val objects = mutableListOf<String>()
            val request = ListObjectsRequest {
                bucket = bucketName
                prefix = path
            }
            S3Client { region = "eu-central-1" }.use { s3 ->
                val response = s3.listObjects(request)
                response.contents?.forEach { obj ->
                    objects.add(obj.key!!)
                }
            }
            if (filter != null) {
                return objects.filter {
                    filter in it
                }
            }
            return objects
        }
    }

}