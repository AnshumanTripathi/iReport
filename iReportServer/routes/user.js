/**
 * Created by AnshumanTripathi on 11/28/16.
 */

var mongoose = require('mongoose');
var User = require("./Schema/UserSchema");
var ec2 = require("./SharedConst").ec2;

mongoose.connection.on('open',function (ref) {
    console.log("Connected to Mongo Server");
});
mongoose.connection.on('error',function (err) {
    console.log("Error Occured: "+err);
    console.log("Retrying.... ")
});

exports.getAllUsers = function (req, res) {
    console.log("Connected to MongoDB. Fetching Data....");
    User.find({}, function (err, users) {
        if (err) {
            console.log("Error occured: " + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else {
            console.log(users);
            res.send({
                statusCode: 200,
                data: users
            });
        }
    });
};

exports.getUser = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    User.findOne({"email": query}, function (err, user) {
        if (err) {
            console.log("Error occured fetching user: " + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else {
            console.log(user);
            res.send({
                statusCode: 200,
                data: user
            });
        }
    });
};

exports.updateSettings = function (req, res) {
    var query = req.body.email;
    console.log("Email received: " + query);
    User.findOne({"email": query}, function (err, user) {
        if (err) {
            console.log("Error Occured: " + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else {
            console.log("User found: " + JSON.stringify(user));
            user.settings.email_confirm = req.body.email_confirm;
            user.settings.email_notify = req.body.email_notify;
            user.settings.anonymous = req.body.anonymous;
            res.send({
                statusCode: 200,
                data: "Settings Updated"
            });
        }
    });
};

exports.addUser = function (req, res) {
    var newUser = new User(req.body.user);
    console.log(newUser);
    newUser.save(function (err) {
        if (err) {
            console.log("Error occured in added: " + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else {
            res.send({
                statusCode: 200,
                data: "New User Added"
            });
        }
    });
};
