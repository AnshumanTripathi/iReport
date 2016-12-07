/**
 * Created by AnshumanTripathi on 12/5/16.
 */

var nodemailer = require('nodemailer');


// setup e-mail data with unicode symbols
var mailOptions = {
    to: 'aggarwal.somya@gmail.com', // list of receivers
    subject: 'Hello ✔', // Subject line
    text: 'Hello world ?', // plaintext body
    html: '<b>Hello world ?</b>' // html body
};

// send mail with defined transport object
exports.sendEmail = function (mailOptions) {

    var transporter = nodemailer.createTransport({
        service: 'Gmail',
        auth:{
            user: 'anshuman.tripathi305@gmail.com',
            pass: 'FCBarcelona10!'
        }
    });
    transporter.sendMail(mailOptions, function(error, info){
        if(error){
            return console.log(error);
        }
        console.log('Message sent: ' + info.response);
    });
};
