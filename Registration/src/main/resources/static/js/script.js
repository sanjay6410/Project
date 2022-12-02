//sgst is used to calculate the bill with sgst
		var totalsgst=0;
		//cgst is used to calculate the bill with cgst
		var totalcgst=0;
		//grandtotal is used to calculate the bill
		var grandTotal=0;
		//fcount is used to count the no of times the page is refreshed
		var fcount=0;
		//this is used to give the specific bill number to every bill
		var billNo="VDCS000000";
		
		//this function is used to trigger the generate bill button
	   const billbutton=document.querySelector("#genbill")
		{
		   const enablebtn=document.getElementById('billbtn');
		   //enablebtn.disabled=true;
			billbutton.onclick = () => {
				fcount++;
				window.location.reload();
			}
		}    
		//this function is used to downlod the bill to customer
		function exportTableToExcel(tableID, filename = ''){
		    var downloadLink;
		    var dataType = 'application/vnd.ms-excel';
		    var tableSelect = document.getElementById(tableID);
		    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
		    
		    //name of the file
		    filename = filename?filename+'.xls':'bill.xls';
		    //download link element
		    downloadLink = document.createElement("a");
		    
		    document.body.appendChild(downloadLink);
		    
		    if(navigator.msSaveOrOpenBlob){
		        var blob = new Blob(['\ufeff', tableHTML], {
		            type: dataType
		        });
		        navigator.msSaveOrOpenBlob( blob, filename);
		    }else{
		    	//link to the file
		        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
		    
		    	//Setting the file name
		        downloadLink.download = filename;
		        
		        //triggering the function
		        downloadLink.click();
		    }
		} 
		//this function is used to show the products in the cart
		var shoppingCart = (function() {
			cart = [];
			
			function Item(name, price, count) {
				this.name = name;
				this.price = price;
				this.count = count;
			}

			//this function is used to save the cart
			function saveCart() {
				sessionStorage.setItem('shoppingCart', JSON.stringify(cart));
			}

			//this function is used to load the cart
			function loadCart() {
				cart = JSON.parse(sessionStorage.getItem('shoppingCart'));
			}
			if (sessionStorage.getItem("shoppingCart") != null) {
				loadCart();
			}

			//empty template literal for adding,removing and count the products
			var obj = {};

			//this is used to add the item to the cart
			obj.addItemToCart = function(name, price, count) {
				for ( var item in cart) {
					if (cart[item].name === name) {
						cart[item].count++;
						saveCart();
						return;
					}
				}
				var item = new Item(name, price, count);
				cart.push(item);
				saveCart();
			}
			//this is used to set the count of the items in the cart
			obj.setCountForItem = function(name, count) {
				for ( var i in cart) {
					if (cart[i].name === name) {
						cart[i].count = count;
						break;
					}
				}
			};
			//this is used to remove the item to the cart
			obj.removeItemFromCart = function(name) {
				for ( var item in cart) {
					if (cart[item].name === name) {
						cart[item].count--;
						if (cart[item].count === 0) {
							cart.splice(item, 1);
						}
						break;
					}
				}
				saveCart();
			}
			obj.removeItemFromCartAll = function(name) {
				for ( var item in cart) {
					if (cart[item].name === name) {
						cart.splice(item, 1);
						break;
					}
				}
				saveCart();
			}

			//this is used to clear all times in  the  the cart
			obj.clearCart = function() {
				cart = [];
				fcount=0;
				saveCart();
			}

			//this is used to totalcount of the items in the cart
			obj.totalCount = function() {
				var totalCount = 0;
				for ( var item in cart) {
					totalCount += cart[item].count;
				}
				return totalCount;
			}

			//this is used to totalcart of the bill and grand total
			obj.totalCart = function() {
				var totalCart = 0;
				for ( var item in cart) {
					totalCart += cart[item].price * cart[item].count;
				}
				totalsgst=0.025*totalCart;
				totalcgst=0.025*totalCart;
				grandTotal=totalCart+totalcgst+totalsgst;
				return Number(grandTotal.toFixed(2));
			}
			

			//this is used to list the cart items
			obj.listCart = function() {
				var cartCopy = [];
				for (i in cart) {
					item = cart[i];
					itemCopy = {};
					for (p in item) {
						itemCopy[p] = item[p];

					}
					itemCopy.total = Number(item.price * item.count).toFixed(2);
					cartCopy.push(itemCopy)
				}
				return cartCopy;
			}
			return obj;
		})();

		//add to cart function
		$('.add-to-cart').click(function(event) {
			event.preventDefault();
			var name = $(this).data('name');
			var price = Number($(this).data('price'));
			shoppingCart.addItemToCart(name, price, 1);
			displayCart();
		});

		//clear cart function
		$('.clear-cart').click(function() {
			shoppingCart.clearCart();
			displayCart();
		});

		//display cart function to display the cart in modal
		function displayCart() {
			var cartArray = shoppingCart.listCart();
			var output = "";
			for ( var i in cartArray) {
				output += "<tr>"
						+ "<td>"
						+ cartArray[i].name
						+ "</td>"
						+ "<td>"
						+ cartArray[i].price
						+ "</td>"
						+ "<td><div class='input-group'><button class='minus-item input-group-addon btn btn-primary' data-name=" + cartArray[i].name + ">-</button>"
						+ "<input type='number' class='item-count form-control' data-name='" + cartArray[i].name + "' value='" + cartArray[i].count + "'>"
						+ "<button class='plus-item btn btn-primary input-group-addon' data-name=" + cartArray[i].name + ">+</button></div></td>"
						+ "<td><button class='delete-item btn btn-danger' data-name=" + cartArray[i].name + ">X</button></td>"
						+ " = " + "<td>" + cartArray[i].total + "</td>"
						+ "</tr>";
			}
			$('.show-cart').html(output);
			$('.total-cart').html(shoppingCart.totalCart());
			$('.total-count').html(shoppingCart.totalCount());
		}
		
		

		//display bill function to display the bill in modal
		function displayBill() {
			//list of products in the cart
			var cartArray = shoppingCart.listCart();
			//random number generation for bill number
			var number=Math.floor(Math.random()*100);
			
			var output = 
			 "<h1 align=right>"+"Retail Delight"+"</h1>"+
			 "<h5 align=right;>"+"Bill No : "+billNo+number+"<h5>"+
		    "<tr>"
		    +"<th>" + "Product Name"+"</th>"
		    +"<th>" + "Product Price"+"</th>"
		    +"<th>" + "Product Count"+"</th>"
		    +"<th>" + "Product Total"+"</th>"
		    +"</tr>";
			for ( var i in cartArray) {
				output +=
					    
					    "<tr>" 
					    + "<td>" + cartArray[i].name+ "</td>"
						+ "<td>" + cartArray[i].price + "</td>" 
						+ "<td>" + cartArray[i].count + "</td>"
						+ "<td>" + cartArray[i].total + "</td>" 
						+ "</tr>";
						
			}

			output+="<tr>"+"<td>"+"SGST (2.5%) = "+totalsgst+"<td>"+"<tr>";
			output+="<tr>"+"<td>"+"CGST (2.5%) = "+totalcgst+"<td>"+"<tr>";
			output+="<tr>"+"<td>"+"GRAND TOTAL = "+grandTotal+"<td>"+"<tr>";
			
			
			 $('.show-bill').html(output); 
		}

		
	//to delete the product in the cart
		$('.show-cart').on("click", ".delete-item", function(event) {
			var name = $(this).data('name')
			shoppingCart.removeItemFromCartAll(name);
			displayCart();
		})

		//to decrease the products in the cart
		$('.show-cart').on("click", ".minus-item", function(event) {
			var name = $(this).data('name')
			shoppingCart.removeItemFromCart(name);
			displayCart();
		})
		//to increase the products in the cart
		$('.show-cart').on("click", ".plus-item", function(event) {
			var name = $(this).data('name')
			shoppingCart.addItemToCart(name);
			displayCart();
		})

		// to show cart
		$('.show-cart').on("change", ".item-count", function(event) {
			var name = $(this).data('name');
			var count = Number($(this).val());
			shoppingCart.setCountForItem(name, count);
			displayCart();
		});

	//display the cart in the modal
		displayCart();
	//display the bill in the modal
		displayBill();