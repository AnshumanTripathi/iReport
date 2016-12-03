/**
 * Created by AnshumanTripathi on 12/2/16.
 */
var Report = require('../schema/ReportSchema');
var User = require('../schema/UserSchema');

exports.addReport = function (req, res) {
    var report = new Report(req.body);
    report.set('status', 'still_there');
    console.log(report);
    report.save(function (err, report) {
        if (err) {
            console.log("Error occured while connecting saving report:" + err);
            res.send({
                statusCode: 400,
                data: err
            });
        } else {
            User.findOne({"email": report.user_email}, function (err, user) {
                if (err) {
                    console.log("Error Occured retrieving user data: " + err);
                    res.send({
                        statusCode: 500,
                        data: err
                    });
                } else if (user == null) {
                    console.log("No user found with the given email: " + err);
                    res.send({
                        statusCode: 400,
                        data: err
                    });
                } else {
                    if (!user.settings.anonymous) {
                        report.isAnonymous = false;
                        user.reports.push(report.id);
                        report.user_screen_name = user.screen_name;
                        report.user_email = user.email;
                    } else {
                        report.isAnonymous = true;
                    }
                    report.save(function (err) {
                        if (err) {
                            console.log("Error Occured in Report saving: " + err);
                            res.send({
                                statusCode: 500,
                                data: err
                            });
                        }
                    });
                    user.save(function (err) {
                        if (err) {
                            console.log("Error occured: " + err);
                            res.send({
                                statusCode: 500,
                                data: err
                            });
                        }
                    });
                }
            });
            console.log("Report Added");
            res.send({
                statusCode: 200,
                data: "Report Added"
            });
        }
    });
};

exports.getAllreports = function (req, res) {
    Report.find({}, function (err, report) {
        if (err) {
            console.log("Error occured in fetching all reports: " + err);
            res.send({
                statusCode: 500,
                data: err
            });
        }
        if (report.length < 1) {
            console.log("No reports found!");
            res.send({
                statusCode: 400,
                data: "No reports in Database"
            });
        } else {
            console.log(report);
            res.send({
                statusCode: 200,
                data: report
            });
        }
    });
};


exports.getReport = function (req, res) {
    var query;
    console.log(req.body);
    if(req.body.hasOwnProperty("status".toLowerCase())){
        query= {status: req.body.status};
    }else if(req.body.hasOwnProperty("email".toLowerCase())){
        query = {user_email: req.body.email}
    }
    console.log(query);
    Report.find(query, function (err, report) {
        if (err) {
            console.log("Error occured in fetching reports: " + err);
            res.send({
                statusCode: 500,
                data: err
            });
        } else if (report.length < 1) {
            console.log("No reports found!");
            res.send({
                statusCode: 400,
                data: "No reports in Database"
            });
        } else {
            console.log(report);
            res.send({
                statusCode: 200,
                data: report
            });
        }
    });
};
