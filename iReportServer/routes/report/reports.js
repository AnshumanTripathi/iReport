/**
 * Created by AnshumanTripathi on 12/2/16.
 */
var Report = require('../schema/ReportSchema');
var User = require('../schema/UserSchema');
var email = require('../email_notify');

exports.addReport = function (req, res) {
    var query = req.body;
    query['status'] = 'still_there';
    var report = new Report(query);
    var jsonResponse;
    console.log(report);
    User.findOne({"email": report.user_email}).lean().exec(function (err, user) {
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
            console.log(user);
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
                console.log(report);
                if (err) {
                    console.log("Error Occured in Report saving: " + err);
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

            if (!user.settings.anonymous && user.settings.email_notify) {
                email.sendEmail({
                    to: user.email,
                    subject: "iReport New Report",
                    text: 'New Report is added',
                    html: '<h2>Hello from iReport!</h2><br><p>New report is added by '
                    + user.email + '<br>Log into iReport app to see updates</p>'
                });
            }
        }
        res.send(jsonResponse);
    });

};

exports.getReports = function (req, res) {
    var query;
    console.log(req.body);
    var jsonResponse = {};
    if (req.body.hasOwnProperty("status".toLowerCase())) {
        query = {status: req.body.status};
    } else if (req.body.hasOwnProperty("email".toLowerCase())) {
        query = {user_email: req.body.email}
    } else if (req.body.hasOwnProperty("location".toLowerCase())) {
        query = {location: req.body.location}
    }
    console.log(query);
    Report.find(query).lean().exec(function (err, report) {
        if (err) {
            console.log("Error occured in fetching reports: " + err);
            jsonResponse = {
                statusCode: 500,
                data: err
            };
        } else if (report.length < 1) {
            console.log("No reports found!");
            jsonResponse = {
                statusCode: 400,
                data: "No reports in Database"
            };
        } else {
            console.log(report);
            for (var i = 0; i < report.length; i++) {
                report[i].timestamp = parseTimestamp(report[i].timestamp);
            }
            jsonResponse = {
                statusCode: 200,
                data: report
            };
        }

        res.send(jsonResponse);
    });
};

exports.getUserReportLocation = function (req, res) {
    var query = req.body.email;
    console.log(query);
    var jsonResponse;
    Report.find({"user_email": query}, function (err, report) {
        if (err) {
            console.log("Error Occured in fetching reports: " + err);
            jsonResponse = {
                statusCode: 500,
                data: err
            };
        } else if (report.length < 1) {
            console.log("No report found from user: " + query);
            jsonResponse = {
                statusCode: 400,
                data: "No report found from user: " + query
            };
        } else {
            var reportsArray = [];
            for (var i = 0; i < report.length; i++) {
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

exports.getReportById = function (req, res) {
    var jsonResponse = {};
    console.log(req.body.id);
    Report.findById(req.body.id).lean().exec(function (err, report) {
        if (err) {
            console.log("Error occured in fetching report: " + err);
            jsonResponse = {
                statusCode: 500,
                data: err
            };
        } else if (report == null) {
            console.log("No report found!");
            jsonResponse = {
                statusCode: 400,
                data: err
            };
        } else {
            console.log(report);

            report.timestamp = parseTimestamp(report.timestamp);
            jsonResponse = {
                statusCode: 200,
                data: report
            };
        }

        res.send(jsonResponse);
    });
};

exports.updateReportStatus = function (req, res) {
    var query = req.body.id,
        updateStatus = req.body.status,
        jsonResponse = {};

    console.log("Query Id: " + query);
    console.log("Status to update: " + updateStatus);
    Report.findById(query, function (err, report) {
        if (err) {
            console.log("Error Occured: " + err);

            jsonResponse = {
                statusCode: 500,
                data: err
            };
        } else if (report == null) {
            console.log("No report found");
            jsonResponse = {
                statusCode: 400,
                data: err
            };
        } else {
            console.log("Report found");
            console.log(report);
            report.status = updateStatus;
            report.save(function (err) {
                if (err) {
                    jsonResponse = {
                        statusCode: 500,
                        data: err
                    };
                }
            });

            User.findOne({"email": report.user_email}, function (err, user) {
                if (err) {
                    console.log("Error occured in finding user: " + err);
                    res.send({
                        statusCode: 500,
                        data: err
                    });
                } else if (user == null) {
                    console.log("No user found!");
                    jsonResponse = {
                        statusCode: 400,
                        data: "No user Found in DB"
                    };
                } else {
                    console.log(user);
                    console.log("Sending Email");
                    if (!user.settings.anonymous && user.settings.email_confirm) {
                        email.sendEmail({
                            to: user.email,
                            subject: "iReport Updated!",
                            html: "<h2>Your iReport has been updated to " + updateStatus +
                            "<br/>Log in iReport app to see updates</h2>"
                        });
                        jsonResponse = {
                            statusCode: 200,
                            data: "Report Updated!"
                        };
                    }
                }
                res.send(jsonResponse);
            });
        }
    });
};

exports.filterReports = function (req, res) {
    var jsonResponse = {};
    if (!req.body.hasOwnProperty('query_email')) {
        var queryStatus = req.body.query_status.split(",");
        console.log(queryStatus);
        Report.find({}).where("status").in(queryStatus)
            .lean().exec(function (err, reports) {
            if (err) {
                console.log("Some Error Occured: " + err);
                jsonResponse = {
                    statusCode: 500,
                    data: err
                };
            } else if (reports.length < 1) {
                console.log("No report found from user");
                jsonResponse = {
                    statusCode: 400,
                    data: "No User found"
                }
            } else {
                for (var i = 0; i < reports.length; i++) {
                    if (reports[i].isAnonymous) {
                        reports[i].email = "anonymous";
                        reports[i].screen_name = "anonymous";
                        reports[i].timestamp = parseTimestamp(reports[i].timestamp);
                    }
                }
                jsonResponse = {
                    statusCode: 200,
                    data: reports
                };
            }
            res.send(jsonResponse);
        });

    } else if (req.body.hasOwnProperty('query_email')) {
        console.log(req.body);
        Report.find({"user_email": req.body.query_email}, function (err, userReport) {
            if (err) {
                console.log("Some Error Occured: " + err);
                jsonResponse = {
                    statusCode: 500,
                    data: err
                };
            } else if (userReport.length < 1) {
                console.log("No report found from user");
                jsonResponse = {
                    statusCode: 400,
                    data: "No User found"
                }
            } else {
                var filterReports = [];
                if (req.body.hasOwnProperty('query_status')) {
                    var requestStatus = req.body.query_status;
                    var temp = requestStatus.split(",");
                    console.log(temp);
                    for (var i = 0; i < userReport.length; i++) {
                        if (temp.indexOf(userReport[i].status) != -1) {
                            console.log(userReport[i]);
                            if (!userReport[i].isAnonymous)
                                userReport[i].timestamp = parseTimestamp(userReport[i].timestamp);
                                filterReports.push(userReport[i]);
                        }
                    }
                } else {
                    for (var i = 0; i < userReport.length; i++) {
                        if (!userReport[i].isAnonymous) {
                            userReport[i].timestamp = parseTimestamp(userReport[i].timestamp);
                            filterReports.push(userReport[i]);
                        }
                    }
                }
                jsonResponse = {
                    statusCode: 200,
                    data: filterReports
                };
            }
            res.send(jsonResponse);
        });
    } else{
        Report.find().lean().exec(function (err, report) {
            if (err) {
                console.log("Error occured in fetching reports: " + err);
                jsonResponse = {
                    statusCode: 500,
                    data: err
                };
            } else if (report.length < 1) {
                console.log("No reports found!");
                jsonResponse = {
                    statusCode: 400,
                    data: "No reports in Database"
                };
            } else {
                console.log(report);
                for (var i = 0; i < report.length; i++) {
                    report[i].timestamp = parseTimestamp(report[i].timestamp);
                }
                jsonResponse = {
                    statusCode: 200,
                    data: report
                };
            }

            res.send(jsonResponse);
        });
    }
};

function parseTimestamp(timestamp) {
    var d = new Date(timestamp),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hour = '' + d.getHours(),
        minutes = '' + d.getMinutes();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if (hour.length < 2) hour = '0' + hour;
    if (minutes.length < 2) minutes = '0' + minutes;

    timestamp = [month, day, year].join("-");
    timestamp += " ";
    timestamp += [hour, minutes].join(":");
    return timestamp;
}
