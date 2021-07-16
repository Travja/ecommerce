let express = require('express');
let router = express.Router();

/* GET home page. */
router.get('/', (req, res, next) => {
    req.session.connected = true;
    res.cookie('sid', req.session.id, {maxAge: 900000});
    res.render('index', {title: 'ECommerce'});
});

router.get('/destroy', (req, res) => {
    req.session.destroy();
    res.redirect('/');
});

module.exports = router;