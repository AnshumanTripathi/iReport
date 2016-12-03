/**
 * Created by AnshumanTripathi on 11/28/16.
 */

var mongoose = require('mongoose');
var User = require("./Schema/UserSchema");

mongoose.connection.on('open', function (ref) {
    console.log("Connected to Mongo Server");
});
mongoose.connection.on('error', function (err) {
    console.log("Error Occured Connecting to Mongo Server: " + err);
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
            user.save(function (err) {
                if (err) {
                    console.log("Error Occured in Updating user settings: " + err);
                    res.send({
                        statusCode: 400,
                        data: err
                    });
                }
                else if (user === undefined) {
                    console.log("No user found with this email");
                    res.send({
                        statusCode: 400,
                        data: "No user found with the given email"
                    });
                } else {
                    console.log("Settings Updated");
                    res.send({
                        statusCode: 200,
                        data: "Settings Updated"
                    });
                }
            });
        }
    });
};

exports.addUser = function (req, res) {
    var newUser = new User(req.body);
    console.log(newUser);
    newUser.save(function (err) {
        if (err) {
            if (err.code === 11000) {
                console.log("Duplicate Property email!!");
                res.send({
                    statusCode: 500,
                    data: "Duplicate Property 'email' "
                });
            } else {
                console.log("Error occured in added: " + err);
                res.send({
                    statusCode: 400,
                    data: err
                });
            }
        } else {
            res.send({
                statusCode: 200,
                data: "New User Added"
            });
        }
    });
};

exports.updateUserInfo = function (req, res) {
    var updatedUserInfo = req.body;
    console.log(updatedUserInfo);
    User.findOne({"email": updatedUserInfo.email}, function (err, user) {
        if (err) {
            console.log("Error in updating user: " + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else if (user === null) {
            console.log("No user found with this email");
            res.send({
                statusCode: 400,
                data: "No user found with the given email"
            });
        }
        else {
            console.log(user);
            user.first_name = updatedUserInfo.first_name;
            user.last_name = updatedUserInfo.last_name;
            user.screen_name = updatedUserInfo.screen_name;
            user.home_address = updatedUserInfo.home_address;
            user.save(function (err) {
                if (err) {
                    console.log("Error Occured in Updating user settings: " + err);
                    res.send({
                        statusCode: 500,
                        data: err
                    });
                }
                else {
                    console.log("Settings Updated");
                    res.send({
                        statusCode: 200,
                        data: "User info Updated"
                    });
                }
            });
        }
    });
};