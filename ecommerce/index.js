let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');
const session = require('express-session');

let indexRouter = require('./routes/index');
let usersRouter = require('./routes/users');

let index = express();

// view engine setup
index.set('views', path.join(__dirname, 'views'));
index.set('view engine', 'pug');

index.use(logger('dev'));
index.use(express.json());
index.use(express.urlencoded({extended: false}));
index.use(cookieParser());
index.use(express.static(path.join(__dirname, 'public')));
// Session Setup
index.use(session({

    // It holds the secret key for session
    secret: 'travjasSecret',

    // Forces the session to be saved
    // back to the session store
    resave: true,

    // Forces a session that is "uninitialized"
    // to be saved to the store
    saveUninitialized: true
}));

index.use('/', indexRouter);
index.use('/users', usersRouter);

// catch 404 and forward to error handler
index.use(function (req, res, next) {
    next(createError(404));
});

// error handler
index.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = index;
