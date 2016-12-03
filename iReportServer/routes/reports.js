/**
 * Created by AnshumanTripathi on 12/2/16.
 */
var Report = require('./Schema/ReportSchema');
var User = require('./Schema/UserSchema');

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
                        if(err){
                            console.log("Error occured: "+err);
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
