/**
 * Created by AnshumanTripathi on 12/2/16.
 */
var Report = require('../schema/ReportSchema');
var User = require('../schema/UserSchema');
var email = require('../emailNotify');

exports.addReport = function (req, res) {
    var report = new Report(req.body);
    var jsonResponse;
    report.set('status', 'still_there');
    console.log(report);
    User.findOne({"email": report.user_email}, function (err, user) {
        if (err) {
            console.log("Error Occured retrieving user data: " + err);
            jsonResponse = {
                statusCode: 500,
                data: err
            };
        } else if (user == null) {
            console.log("No user found with the given email: " + report.user_email);
            jsonResponse = {
                statusCode: 400,
                data: "No user found with the given email: " + report.user_email
            };
        } else {
            console.log("User found");
            if (!user.settings.anonymous) {
                report.isAnonymous = false;
                user.reports.push(report.id);
                report.user_screen_name = user.screen_name;
                report.user_email = user.email;
            } else {
                report.isAnonymous = true;
            }
            report.save(function (err) {
                console.log("Saving report");
                if (err) {
                    console.log("Error Occured in Report saving: " + err);
                    jsonResponse = {
                        statusCode: 500,
                        data: err
                    };
                }
            });
            user.save(function (err) {
                if (err) {
                    console.log("Error occured: " + err);
                    jsonResponse = {
                        statusCode: 500,
                        data: err
                    };
                }
            });
            jsonResponse = {
                statusCode: 200,
                data: "Report Added"
            };

            if(!user.settings.anonymous) {
                email.sendEmail({
                    to: user.email,
                    subject: "iReport New Report",
                    text: 'New Report is added',
                    html: '<h2>Hello from iReport!</h2><br><p>New report is added by ' + user.email + '<br>Log into iReport app to see updates</p>'
                });
            }
        }
        res.send(jsonResponse);
    });

};

exports.getReports = function (req, res) {
    var query;
    console.log(req.body);
    if (req.body.hasOwnProperty("status".toLowerCase())) {
        query = {status: req.body.status};
    } else if (req.body.hasOwnProperty("email".toLowerCase())) {
        query = {user_email: req.body.email}
    } else if (req.body.hasOwnProperty("location".toLowerCase())) {
        query = {location: req.body.location}
    }
    Report.find(query).lean().exec(function (err, report) {
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
            for(var i = 0; i<report.length; i++) {
                var d = new Date(report[i].timestamp),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear(),
                    hour = '' + d.getHours(),
                    minutes = ''+d.getMinutes();

                if (month.length < 2) month = '0' + month;
                if (day.length < 2) day = '0' + day;
                if (hour.length < 2) hour = '0'+ hour;
                if (minutes.length < 2) minutes = '0' + minutes;

                report[i].timestamp = [month,day,year].join("-");
                report[i].timestamp += " ";
                report[i].timestamp += [hour, minutes].join(":");
            }
            res.send({
                statusCode: 200,
                data: report
            });
        }
    });
};

exports.getUserReportLocation = function (req,res) {
    var query = req.body.email;
    console.log(query);
    var jsonResponse;
    Report.find({"user_email":query},function (err,report) {
        if(err){
            console.log("Error Occured in fetching reports: "+err);
            jsonResponse = {
                statusCode: 500,
                data: err
            };
        }else if(report.length < 1){
            console.log("No report found from user: "+query);
            jsonResponse = {
                statusCode: 400,
                data: "No report found from user: "+query
            };
        } else{
            var reportsArray = [];
            for(var i = 0; i<report.length;i++){
                reportsArray.push(report[i].location);
            }
            jsonResponse = {
                statusCode: 200,
                data: reportsArray
            };
        }

        res.send(jsonResponse);
    });
};

exports.getReportById = function (req,res) {
    var jsonResponse = {};
    console.log(req.body.id);
    Report.findById(req.body.id,function (err,report) {
        if(err){
            console.log("Error occured in fetching report: "+err);
            jsonResponse = {
              statusCode: 500,
                data:err
            };
        }else if(report == null){
            console.log("No report found!");
            jsonResponse = {
                statusCode: 400,
                data:err
            };
        }else{
            console.log(report);
            jsonResponse = {
                statusCode:200,
                data:report
            };
        }
        res.send(jsonResponse);
    });
};