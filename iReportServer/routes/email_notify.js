/**
 * Created by AnshumanTripathi on 12/5/16.
 */

var nodemailer = require('nodemailer');
var credentials = require('./Credentials').useremail;


// send mail with defined transport object
exports.sendEmail = function (mailOptions) {

    var transporter = nodemailer.createTransport({
        service: 'Gmail',
        auth:{
            user: credentials.email,
            pass: credentials.pass
        }
    });
    transporter.sendMail(mailOptions, function(error, info){
        if(error){
            return console.log(error);
        }
        console.log('Message sent: ' + info.response);
    });
};



