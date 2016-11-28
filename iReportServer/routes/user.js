/**
 * Created by AnshumanTripathi on 11/28/16.
 */
var client = require('mongodb').MongoClient;
var ec2 = "ec2-35-164-176-121.us-west-2.compute.amazonaws.com";

exports.getAllUsers = function (req, res) {
    client.connect("mongodb://" + ec2 + ":27017/iReport", function (err, db) {
        if (err) {
            console.log("Error Occured");
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            var collection = db.collection("user");
            collection.find().toArray(function (err, results) {
                console.log("in find");
                if (err) {
                    console.log("Error in find: " + err);
                } else {
                    console.log(results);
                    res.send(results);
                    db.close();
                }
            });
        }
    });
};

exports.getUser = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    client.connect("mongodb://" + ec2 + ":27017/iReport", function (err, db) {
        if (err) {
            console.log("Error Occured: " + err);
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            var collection = db.collection("user");
            collection.findOne({"email": "" + query}, function (err, results) {
                if (err) {
                    console.log("Error in find: " + err);
                } else {
                    console.log(results);
                    res.send(results);
                    db.close();
                }
            });
        }
    });
};

exports.updateSettings = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    client.connect("mongodb://" + ec2 + ":27017/iReport", function (err, db) {
        if (err) {
            console.log("Error Occured: " + err);
        } else {
            console.log("Connected to MongoDB. Fetching Data....");
            var collection = db.collection("user");
            collection.updateOne({"email": "" + query},
                {
                    $set: {
                        "email_confirm": req.body.email_confirm,
                        "email_notify": req.body.email_notify,
                        "anonymous": req.body.anonymous
                    }
                }, function (err, results) {
                    if (err) {
                        console.log("Error in find: " + err);
                    } else {
                        console.log(results);
                        res.send(results);
                        db.close();
                    }
                });
        }
    });
};
