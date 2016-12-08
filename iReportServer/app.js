var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var http = require("http");
var mongoose = require("mongoose");
var index = require('./routes/index');
var user = require('./routes/user/user');
var reportTest = require("./routes/report/reportTest");
var report = require('./routes/report/reports');
var host = require("./routes/SharedConst").host;
var app = express();

//Create Mongo Connection
mongoose.connect("mongodb://" + host + ":27017/iReport");

// view engine setup
app.set('port', process.env.PORT || 3000);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);

//official API
app.get('/getAllUsers',user.getAllUsers);
app.post('/getUser',user.getUser);

//User API
app.post('/updateSettings',user.updateSettings);
app.post('/addUser',user.addUser);
app.post('/updateUserInfo',user.updateUserInfo);

//My Report API
app.post('/addReport',report.addReport);
app.post('/getReports',report.getReports);
app.post('/getUserReportLocation',report.getUserReportLocation);

//TestAPIs
app.post('/testPic',function (req,res) {
    console.log(req.body);
    var img = req.body.images;
    res.write(img);
    res.end();
});
app.get('/getFile',reportTest.getFile);
app.post('/testGrid',reportTest.uploadFile);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});


// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.send('Wrong Invocation');
});

http.createServer(app).listen(app.get('port'), function(){
    console.log('Express server listening on port ' + app.get('port'));
});

module.exports = app;
