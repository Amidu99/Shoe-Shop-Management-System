import {EmployeeServiceUrl, SaleServiceUrl, InventoryServiceUrl, StockServiceUrl} from "../assets/js/urls.js";
import {showSwalError} from "../assets/js/notifications.js";
export let employeeName;
let mostSellItemQty = null;
let mostSellItemCode = null;
let mostSellItemProfit = null;
let base64MostSellItemPic = null;

document.addEventListener('DOMContentLoaded', async function() {
    try {
        await getEmployeeByEmail();
        await getLowStockAlerts();
        await getShowcaseItems();
    } catch (error) {
        console.error('Error fetching employee data:', error);
    }
});

async function getEmployeeByEmail() {
    let email = localStorage.getItem('CurrentUser');
    const url = new URL(`${EmployeeServiceUrl}/getByEmail`);
    try {
        const response = await fetch(url, {method: 'GET', headers: {"email": email, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
        if (response.status === 200) {
            const data = await response.json();
            employeeName = data.employeeName;
            $("#current_user_name").text('Hi , '+employeeName);
        } else {console.error('Error:', response.status, response.statusText);}
    } catch (error) {console.error('Error:', error);}
}

export const getLowStockAlerts = () => {
    const getAllURL = new URL(`${StockServiceUrl}/getLowStocks`);
    fetch(getAllURL, { method: 'GET', headers:{"Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                $('#alert_tbl_body').empty();
                data.forEach(stock => {
                    let statusColor;
                    if (stock.status === 'Low') { statusColor = '#FF7E00FF';}
                    else if (stock.status === 'Not Available') { statusColor = 'rgba(224,4,4,0.8)';}
                    else { statusColor = 'black';}
                    let record = `<tr><td>${stock.inventory.itemCode}</td><td>${stock.size}</td><td style="color: ${statusColor};">${stock.status}</td></tr>`;
                    $("#alert_tbl_body").append(record);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
        .catch(error => { console.error('Error: ', error); $('#alert_tbl_body').empty(); });
};

const getShowcaseItems = () => {
    const getAllURL = new URL(`${InventoryServiceUrl}/getAll`);
    fetch(getAllURL, { method: 'GET', headers: { "Authorization": "Bearer " + localStorage.getItem("AuthToken")}})
        .then(response => {
            if (!response.ok) { throw new Error(`HTTP error! Status: ${response.status}`); }
            return response.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                const cardContainer = document.getElementById('card-container');
                data.forEach(inventory => {
                const cardElement = document.createElement('div');
                cardElement.classList.add('col');
                cardElement.innerHTML = `
                    <div class="card">
                        <img src="${inventory.itemPic}" class="card-img-top" alt="${inventory.itemCode}">
                        <div class="card-body">
                            <h5 class="card-title">${inventory.itemCode}</h5>
                            <p class="card-text">${inventory.itemDesc}</p>
                            <h6>Rs. ${inventory.sellPrice}/=</h6>
                        </div>
                    </div>
                `;
                cardContainer.appendChild(cardElement);
                });
            } else { console.error('Error: Expected JSON array, but received: ', data); }
        })
        .catch(error => { console.error('Error: ', error); });
};

document.addEventListener('DOMContentLoaded', (event) => {
    const dateInput = document.getElementById('check_date');
    dateInput.addEventListener('change', async function() {
        const selectedDate = this.value;
        await fetchSaleCountByDate(selectedDate);
        await fetchMostSaleItemByDate(selectedDate);
        await getItemByItemCode(mostSellItemCode);
        calculateTheProfit();
    });

    async function fetchSaleCountByDate(date) {
        const url = new URL(`${SaleServiceUrl}/getSaleCount`);
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: { "day": date, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}
            });
            if (response.status === 200) {
                document.getElementById("total_sales").innerHTML = await response.text();
            } else if (response.status === 403) {
                showSwalError('Forbidden', 'You do not have permission to perform this action!');
            } else { console.error('Error:', response.status, response.statusText);}
        } catch (error) { console.error('Error:', error);}
    }

    async function fetchMostSaleItemByDate(date) {
        const url = new URL(`${SaleServiceUrl}/getMostSaleItem`);
        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: { "day": date, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}
            });
            if (response.ok) {
                const text = await response.text();
                if (text) {
                    const data = JSON.parse(text);
                    const itemCode = data.itemCode;
                    const totalQty = data.totalQty;
                    mostSellItemCode = itemCode;
                    mostSellItemQty = totalQty;
                    document.getElementById("most_sale_item").innerHTML = itemCode;
                    document.getElementById("most_sale_item_qty").innerHTML = totalQty;
                } else {
                    mostSellItemCode = null;
                    mostSellItemQty = null;
                    document.getElementById("most_sale_item").innerHTML = 'No Content';
                    document.getElementById("most_sale_item_qty").innerHTML = 'No Content';
                }
            } else if (response.status === 403) { showSwalError('Forbidden', 'You do not have permission to perform this action!');
            } else { console.error('Error:', response.status, response.statusText); }
        } catch (error) { console.error('Error:', error); }
    }

    async function getItemByItemCode(mostSellItemCode) {
        if (mostSellItemCode!==null) {
            const url = new URL(`${InventoryServiceUrl}/get`);
            try {
                const response = await fetch(url, {method: 'GET', headers: {"itemCode": mostSellItemCode, "Authorization": "Bearer " + localStorage.getItem("AuthToken")}});
                const text = await response.text();
                if (text) {
                    const data = JSON.parse(text);
                    base64MostSellItemPic = data.itemPic;
                    mostSellItemProfit = data.profit;
                    if(base64MostSellItemPic){ $('#preview_most_item_pic').attr('src', base64MostSellItemPic).css('display', 'block');}
                }
            } catch (error) {console.error('Error:', error);}
        } else {
            $('#preview_most_item_pic').attr('src', '../../assets/images/noImage.jpg').css('display', 'block');;
        }
    }

    function calculateTheProfit() {
        if(mostSellItemProfit && mostSellItemQty){
            let profit = (mostSellItemProfit * mostSellItemQty);
            document.getElementById("total_profit").innerHTML = 'Rs.'+profit;
        }else{
            document.getElementById("total_profit").innerHTML = 'No Content';
        }
    }
});

// Logout
$("#btn-logout").on("click", () => {
    Swal.fire({
        width: '300px', title: 'Logout', icon: 'question',
        text: "Are you sure you want to logout?",  iconColor: '#FF7E00FF',
        showCancelButton: true, confirmButtonText: 'Logout'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('AuthToken');
            localStorage.removeItem('RefreshToken');
            localStorage.removeItem('CurrentUser');
            window.location.href = '../login/login.html';
            history.pushState(null, null, '../login/login.html');
            window.addEventListener('popstate', function(event) {
                history.pushState(null, null, '../login/login.html');
            });
        }
    });
});