/**
 * Created by AnshumanTripathi on 11/28/16.
 */

var client = require('mongodb').MongoClient;
var mongoose = require('mongoose');
var ec2 = require("./SharedConst").ec2;
var User = require("./Schema");

mongoose.connect("mongodb://" + ec2 + ":27017/iReport")

exports.getAllUsers = function (req, res) {
    console.log("Connected to MongoDB. Fetching Data....");
    User.find({}, function (err, users) {
        if (err) {
            console.log("Error occured: " + err);
        }
        console.log(users);
        res.send(users);
    });
};

exports.getUser = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    User.findOne({"email":query} , function(error,user){
        if(error){
            console.log("Error occured fetching user: "+error);
        }else {
            console.log(user);
            res.send(user);
        }
    });
};

exports.updateSettings = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    User.findOne({"email":query},function (err,user) {
        if(err){
            console.log("Error Occured: "+err);
        }else{
            console.log("User found: "+JSON.stringify(user));
            user.settings.email_confirm = req.body.email_confirm;
            user.settings.email_notify = req.body.email_notify;
            user.settings.anonymous = req.body.anonymous;
            res.send("Settings Updated!");
        }
    });
};

exports.addUser = function (req, res) {
    var newUser = new User(req.body.user);
    newUser.save(function (err) {
        if (err) {
            console.log("Error occured in added: " + err);
        } else {
            res.send("New User Added!");
        }
    });
};
