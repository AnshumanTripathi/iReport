/**
 * Created by AnshumanTripathi on 11/28/16.
 */
var client = require('mongodb').MongoClient;
var ec2 = "ec2-35-164-176-121.us-west-2.compute.amazonaws.com";

exports.findUser = function (req, res) {
    client.connect("mongodb://"+ec2+":27017/test", function (err, db) {
        if (err) {
            console.log("Error Occured");
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            var collection = db.collection("testcoll");
            collection.find().toArray(function (err, results) {
                console.log("in find");
                if (err) {
                    console.log("Error in find: " + err);
                } else {
                    console.log(results);
                    res.send(results);
                }
            });
        }
    });
};
