/**
 * Created by AnshumanTripathi on 11/28/16.
 */
var fs = require("fs");
var mongo = require("mongodb");
var client = require("mongodb").MongoClient;
var grid = require("gridfs-stream");
var ec2 = require("./SharedConst").ec2;
var path = require('path');
var filePath = path.join(__dirname,'./bat.jpg');
exports.uploadFile = function (req,res) {
    client.connect("mongodb://" + ec2 + ":27017/test", function (err, db) {
        if (err) {
            console.log("Error Occured");
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            // var collection = db.collection("fileTest");
            var gridfs = grid(db,mongo);
            var writestream = gridfs.createWriteStream({
                filename: "batman.jpg",
            });
            fs.createReadStream(filePath).pipe(writestream);
           // req.pipe(gridfs.createWriteStream({
           //     filename: "test.txt",
           //     root: "testFileColl",
           //     content_type: 'plain/text'
           // }));
            writestream.on("close",function (file) {
               console.log(file.filename + " written to DB")
            });
           res.send("success");
        }
    });
};

exports.getFile  = function (req, res) {
    client.connect("mongodb://" + ec2 + ":27017/test", function (err, db) {
        if (err) {
            console.log("Error Occured");
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            var readFs = fs.createWriteStream(path.join(__dirname,"../writeto/readbat.jpg"));
            // var collection = db.collection("fileTest");
            var gridfs = grid(db,mongo);
            var readStream = gridfs.createReadStream({
                filename: "batman.jpg"
            });
           readStream.pipe(readFs);
           readFs.on("close",function () {
               console.log("File written successfully");
           });
           res.send("success");
        }
    });
};