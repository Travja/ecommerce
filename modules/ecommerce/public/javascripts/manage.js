const orders = document.getElementById('orders');

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
            'Authorization': 'Basic ' + btoa('manager:admin'),
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

let saveOrder = order => {
    sendJsonRequest('http://' + window.location.hostname + ':8070/order-service/order/update', 'POST', order)
        .then(res => {
            if (res.ok) {
                console.log('Successfully POSTed to cart.');
            } else {
                alert('It seems that the server is having trouble. Please wait a bit and try again.');
            }
        });
};

let getOrders = () => {
    fetch('http://' + window.location.hostname + ':8070/order-service/order', {
        headers: {
            'Authorization': 'Basic ' + btoa('manager:admin')
        }
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            console.log(data);
            if (!data) {
                console.log('Received null data');
                return;
            }
            data.forEach(order => {
                let total = 0;
                for (let itm of order.cart.items) {
                    console.log(itm);
                    total += itm.qty * itm.item.unitPrice;
                }

                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', order.id));
                elm.appendChild(createElement('td', order.cart.items.length));
                elm.appendChild(createElement('td', '$' + total.toFixed(2)));
                elm.appendChild(createElement('td', order.name));
                elm.appendChild(createElement('td', order.email));
                elm.appendChild(createElement('td', order.address));
                let shipCell = createElement('td', (order.shippedBy ? order.shippedBy : 'Not Shipped'));
                let trackingCell = createElement('td', (order.trackingNumber == 0 ? 'Not Shipped' : order.trackingNumber));
                shipCell.contentEditable = true;
                trackingCell.contentEditable = true;
                elm.appendChild(shipCell);
                elm.appendChild(trackingCell);
                elm.appendChild(createElement('td', createButton(null, 'Save', evt => {
                    order.shippedBy = shipCell.innerHTML;
                    order.trackingNumber = trackingCell.innerHTML;
                    console.log(order.shippedBy, order.trackingNumber);
                    saveOrder(order);
                })));
                orders.appendChild(elm);
            });
        })
        .catch(err => {
            console.log(err);
        });
};

getOrders();
