let express = require('express');
let router = express.Router();

const urlencodedParser = express.urlencoded({extended: false});

let checkAuth = async (req, res, next) => {
    if (req.session.user && req.session.user.isAuthenticated) {
        next();
    } else {
        res.redirect('/login');
    }
};

/* GET home page. */
router.get('/', (req, res, next) => {
    req.session.connected = true;
    res.cookie('sid', req.session.id, {maxAge: 900000});
    res.render('index', {title: 'ECommerce'});
});

router.get('/login', (req, res) => {
    res.render('login', {title: 'Login'});
});

router.post('/login', urlencodedParser, (req, res) => {
    console.log(req.body.username);
    console.log(req.body.password);
    if (req.body.username == 'manager' && req.body.password == 'admin') {
        req.session.user = {
            isAuthenticated: true,
            username: req.body.username
        }
        res.redirect('/manage');
    } else {
        res.redirect('/login');
    }
});

router.get('/manage', checkAuth, (req, res) => {
    res.render('manage', {
        user: {
            username: "manager",
            password: "admin"
        },
        title: "Management"
    });
});

router.get('/destroy', (req, res) => {
    req.session.destroy();
    res.redirect('/');
});

module.exports = router;