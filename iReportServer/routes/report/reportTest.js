/**
 * Created by AnshumanTripathi on 11/28/16.
 */
var fs = require("fs");
var mongoose = require('mongoose');

var grid = require("gridfs-stream");
var host = require("../SharedConst").host;
var path = require('path');
var filePath = path.join(__dirname, './bat.jpg');

grid.mongo = mongoose.mongo;

exports.uploadFile = function (req, res) {

    var conn = mongoose.createConnection(host, 'iReport');
    conn.once('open', function (error) {
        if(error){
            throw error;
        }else {
            console.log("Connected to MongoDB. Fetching Data....");
            // var collection = db.collection("fileTest");
            var gridfs = grid(conn.db);
            var writestream = gridfs.createWriteStream({
                filename: "batman.jpg"
            });
            fs.createReadStream(filePath).pipe(writestream);
            writestream.on("close", function (file) {
                console.log(file.filename + " written to DB")
            });
            res.send("success");
        }
    });

};

exports.getFile = function (req, res) {
    var conn = mongoose.createConnection(host, 'iReport');
    conn.once('open', function () {
        console.log("Connected to MongoDB. Fetching Data....");
        var readFs = fs.createWriteStream(path.join(__dirname, "../writeto/bat.jpg"));
        // var collection = db.collection("fileTest");
        var gridfs = grid(conn.db);
        var readStream = gridfs.createReadStream({
            filename: "batman.jpg"
        });
        readStream.pipe(readFs);
        readFs.on("close", function () {
            console.log("File written successfully");
        });
        res.send("success");
    });

};