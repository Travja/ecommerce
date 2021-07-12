const defCart = document.getElementById('cart').innerHTML;

let createElement = (tagName, contents) => {
    let elm = document.createElement(tagName);
    if (typeof (contents) == 'object')
        elm.appendChild(contents);
    else
        elm.innerHTML = contents;

    return elm;
};

let createAddButton = item => {
    let btn = document.createElement('button');
    btn.innerText = 'Add to Cart';
    let sid = getCookie('sid');

    btn.onclick = evt => {
        console.log(item.id);
        item.qty = 1;
        fetch('http://127.0.0.1:8081/cart/' + sid + '/additem', {
            method: 'POST',
            body: JSON.stringify(item),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            console.log('Successfully POSTed to cart.', res);

            updateCart();
        });
    };

    return btn;
};

let createRemoveButton = item => {
    let btn = document.createElement('button');
    btn.innerText = 'Add to Cart';
    let sid = getCookie('sid');

    btn.onclick = evt => {
        console.log(item.id);
        item.qty = 1;
        fetch('http://127.0.0.1:8081/cart/' + sid + '/removeitem', {
            method: 'POST',
            body: JSON.stringify(item),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => {
            console.log('Successfully removed item from cart.', res);

            updateCart();
        });
    };

    return btn;
};

let getItems = () => {
    fetch('http://127.0.0.1:8080/api/items')
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (!data) {
                console.log('Received null data');
                return;
            }
            console.log(data);
            let tbl = document.getElementById('items');
            data.forEach(item => {
                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', item.id));
                elm.appendChild(createElement('td', item.title));
                elm.appendChild(createElement('td', item.description));
                elm.appendChild(createElement('td', '$' + item.unitPrice.toFixed(2)));
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

    fetch('http://127.0.0.1:8081/cart/' + sid)
        .then(response => {
            console.log(response);
            return response.json();
        })
        .then(data => {
            if (!data) {
                console.log('Cart info not found.');
                return;
            }
            console.log(data);
            let tbl = document.getElementById('cart');
            document.getElementById('cart').innerHTML = defCart;
            data.items.forEach(item => {
                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', item.id));
                elm.appendChild(createElement('td', item.title));
                elm.appendChild(createElement('td', item.description));
                elm.appendChild(createElement('td', item.qty));
                elm.appendChild(createElement('td', '$' + (item.unitPrice * item.qty).toFixed(2)));
                elm.appendChild(createElement('td', createRemoveButton(item)));
                tbl.appendChild(elm);
            });
        })
        .catch(err => {
            console.log(err);
        });
};

function getCookie(cname) {
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
}

getItems();
updateCart();