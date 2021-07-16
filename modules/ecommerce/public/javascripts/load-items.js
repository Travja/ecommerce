const defCart = document.getElementById('cart').innerHTML;
const defCatalog = document.getElementById('items').innerHTML;

let createElement = (tagName, contents) => {
    let elm = document.createElement(tagName);
    if (typeof (contents) == 'object')
        elm.appendChild(contents);
    else
        elm.innerHTML = contents;

    return elm;
};

let sendJsonRequest = (url, method, body) => {
    return fetch(url, {
        method,
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

let createButton = (id, text, action) => {
    let btn = document.createElement('button');
    btn.innerText = text;
    if (id)
        btn.id = id;

    btn.onclick = action;

    return btn;
};

let addItem = item => {
    let sid = getCookie('sid');
    let send = {
        item,
        qty: 1
    }
    console.log(send);
    sendJsonRequest('http://' + window.location.hostname + ':8081/cart/' + sid + '/additem', 'POST', send)
        .then(res => {
            console.log('Successfully POSTed to cart.');
            updateCart();
        });
};

let createAddButton = item => {
    return createButton(null, 'Add to Cart', evt => addItem(item));
};

let createRemoveButton = item => {
    return createButton(null, 'Remove', evt => {
        let sid = getCookie('sid');
        sendJsonRequest('http://' + window.location.hostname + ':8081/cart/' + sid + '/removeitem', 'POST', item)
            .then(res => {
                console.log('Successfully removed item from cart.', res);

                updateCart();
            });
    });
};

let getItems = () => {
    fetch('http://' + window.location.hostname + ':8080/api/items')
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (!data) {
                console.log('Received null data');
                return;
            }
            let tbl = document.getElementById('items');
            data.forEach(item => {
                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', item.id));
                elm.appendChild(createElement('td', item.title));
                elm.appendChild(createElement('td', item.description));
                elm.appendChild(createElement('td', '$' + item.unitPrice));
                elm.appendChild(createElement('td', createAddButton(item)));
                tbl.appendChild(elm);
            });
        })
        .catch(err => {
            console.log(err);
        });
};

let searchItems = (title) => {
    fetch('http://' + window.location.hostname + ':8080/api/items/search?title=' + title)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (!data) {
                console.log('Received null data');
                return;
            }
            let tbl = document.getElementById('items');
            tbl.innerHTML = defCatalog;
            data.forEach(item => {
                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', item.id));
                elm.appendChild(createElement('td', item.title));
                elm.appendChild(createElement('td', item.description));
                elm.appendChild(createElement('td', '$' + item.unitPrice));
                elm.appendChild(createElement('td', createAddButton(item)));
                tbl.appendChild(elm);
            });
        })
        .catch(err => {
            console.log(err);
        });
};

let updateCart = () => {
    let sid = getCookie('sid');

    fetch('http://' + window.location.hostname + ':8081/cart/' + sid)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (!data) {
                console.log('Cart info not found.');
                return;
            }
            let tbl = document.getElementById('cart');
            document.getElementById('cart').innerHTML = defCart;
            let subtotal = 0;
            if (data.items)
                data.items.forEach(it => {
                    let item = it.item;
                    let elm = document.createElement('tr');
                    elm.appendChild(createElement('td', item.id));
                    elm.appendChild(createElement('td', item.title));
                    elm.appendChild(createElement('td', item.description));

                    let quantity = document.createElement('div');
                    quantity.classList = ['qtyCell'];
                    quantity.appendChild(createButton(null, '-', evt => {
                        let send = {
                            item,
                            qty: 1
                        }
                        console.log(send);
                        sendJsonRequest('http://' + window.location.hostname + ':8081/cart/' + sid + '/removeitem', 'POST', send)
                            .then(res => {
                                console.log('Successfully removed item from cart.', res);

                                updateCart();
                            });
                    }));
                    let qty = document.createElement('div');
                    qty.innerHTML = it.qty;
                    qty.classList = ['qty'];
                    quantity.appendChild(qty);
                    quantity.appendChild(createButton(null, '+', evt => addItem(item)));

                    elm.appendChild(createElement('td', quantity));
                    let cost = (item.unitPrice * it.qty);
                    subtotal += cost;
                    elm.appendChild(createElement('td', '$' + cost.toFixed(2)));
                    elm.appendChild(createElement('td', createRemoveButton(it)));
                    tbl.appendChild(elm);
                });

            document.getElementById('subtotal').innerHTML = "Subtotal: $" + subtotal.toFixed(2);
        })
        .catch(err => {
            console.log(err);
        });
};

let getCookie = (cname) => {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
};

let getCart = (cname, callback) => {
    fetch('http://' + window.location.hostname + ':8081/cart/' + cname)
        .then(res => res.json())
        .then(data => {
            callback(data);
        })
        .catch(err => {
            console.log('Could not get cart data for cart ' + cname);
        });
};

getItems();
updateCart();

document.getElementById('checkoutBtn').onclick = () => {
    let href = 'http://' + window.location.hostname + ':8082/checkout';

    let name = document.getElementById('name').value;
    let email = document.getElementById('email').value;
    let address = document.getElementById('address').value;
    let creditNum = document.getElementById('creditnum').value;
    let expDate = new Date(document.getElementById('expdate').value);
    expDate.setMonth(expDate.getMonth() + 1);
    expDate = new Date(expDate.getMilliseconds() - 1000).toUTCString();
    let cvv = document.getElementById('cvv').value;

    let cardInfo = {
        cardNumber: creditNum,
        expirationDate: expDate,
        cvv
    }

    getCart(getCookie('sid'), cart => {
        let info = {
            name,
            email,
            address,
            cardInfo,
            cart
        };

        sendJsonRequest(href, 'POST', info)
            .then(res => {
                console.log(res);
                console.log(res.status);
                if (res.status == 200)
                    window.location = window.location.origin + "/destroy";
            });
        console.log('Sent request to checkout!');
    });

    console.log(href);
};

document.getElementById('searchBtn').onclick = evt => {
    console.log('Searching');
    searchItems(document.getElementById('search').value);
};