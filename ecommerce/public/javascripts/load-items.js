let createElement = (tagName, contents) => {
    let elm = document.createElement(tagName);
    if (typeof (contents) == 'object')
        elm.appendChild(contents);
    else
        elm.innerHTML = contents;

    return elm;
};

let createButton = id => {
    let btn = document.createElement('button');
    btn.innerText = 'Add to Cart';

    btn.onclick = evt => {
        console.log(id);
        //TODO Send request to cart service to add item
    };

    return btn;
};

let getItems = () => {
    fetch('http://localhost:8080/api/items')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            let tbl = document.getElementById('items');
            data.forEach(item => {
                let elm = document.createElement('tr');
                elm.appendChild(createElement('td', item.id));
                elm.appendChild(createElement('td', item.title));
                elm.appendChild(createElement('td', item.description));
                elm.appendChild(createElement('td', '$' + item.unitPrice));
                elm.appendChild(createElement('td', createButton(item.id)));
                tbl.appendChild(elm);
            });
        });
};

getItems();