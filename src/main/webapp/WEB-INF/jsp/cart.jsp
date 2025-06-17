<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - KeyCraft</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-keyboard"></i> KeyCraft
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/products">Products</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" href="/cart">
                            <i class="fas fa-shopping-cart"></i> Cart
                            <span class="badge bg-primary" id="cartCount" style="display: none;">0</span>
                        </a>
                    </li>
                    <c:if test="${currentUser != null}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="fas fa-user"></i> ${currentUser.firstName} ${currentUser.lastName}
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="/orders">My Orders</a></li>
                                <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                            </ul>
                        </li>
                    </c:if>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Cart Content -->
    <div class="container my-4">
        <h2><i class="fas fa-shopping-cart"></i> Shopping Cart</h2>
        
        <div class="row">
            <div class="col-lg-8">
                <div id="cartItems">
                    <!-- Cart items will be loaded here -->
                </div>
                
                <div id="emptyCart" class="text-center py-5" style="display: none;">
                    <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                    <h4>Your cart is empty</h4>
                    <p class="text-muted">Start shopping to add items to your cart.</p>
                    <a href="/products" class="btn btn-primary">Continue Shopping</a>
                </div>
            </div>
            
            <div class="col-lg-4">
                <div class="card" id="cartSummary" style="display: none;">
                    <div class="card-header">
                        <h5><i class="fas fa-calculator"></i> Order Summary</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between mb-2">
                            <span>Subtotal:</span>
                            <span id="subtotal">$0.00</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Shipping:</span>
                            <span class="text-success">FREE</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between mb-3">
                            <strong>Total:</strong>
                            <strong id="total">$0.00</strong>
                        </div>
                        <a href="/checkout" class="btn btn-success w-100">
                            <i class="fas fa-credit-card"></i> Proceed to Checkout
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Toast Container -->
    <div class="toast-container position-fixed top-0 end-0 p-3" id="toastContainer"></div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script>
        let cartItems = [];
        
        $(document).ready(function() {
            loadCartItems();
        });
        
        function loadCartItems() {
            $.ajax({
                url: '/api/cart',
                method: 'GET',
                success: function(items) {
                    cartItems = items;
                    displayCartItems();
                    updateCartSummary();
                },
                error: function() {
                    window.location.href = '/login';
                }
            });
        }
        
        function displayCartItems() {
            const cartContainer = $('#cartItems');
            
            if (cartItems.length === 0) {
                $('#emptyCart').show();
                $('#cartSummary').hide();
                cartContainer.empty();
                return;
            }
            
            $('#emptyCart').hide();
            $('#cartSummary').show();
            
            cartContainer.empty();
            
            cartItems.forEach(function(item) {
                const itemTotal = (item.product.price * item.quantity).toFixed(2);
                
                const cartItem = $(`
                    <div class="card mb-3" data-cart-id="${item.id}">
                        <div class="card-body">
                            <div class="row align-items-center">
                                <div class="col-md-2">
                                    <img src="${item.product.imageUrl}" alt="${item.product.name}" 
                                         class="img-fluid rounded" style="height: 80px; object-fit: cover;">
                                </div>
                                <div class="col-md-4">
                                    <h6 class="mb-1">${item.product.name}</h6>
                                    <small class="text-muted">${item.product.brand}</small>
                                    <div class="text-primary fw-bold">$${item.product.price}</div>
                                </div>
                                <div class="col-md-3">
                                    <div class="input-group">
                                        <button class="btn btn-outline-secondary" type="button" 
                                                onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                                        <input type="text" class="form-control text-center" 
                                               value="${item.quantity}" readonly>
                                        <button class="btn btn-outline-secondary" type="button" 
                                                onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                                    </div>
                                </div>
                                <div class="col-md-2 text-end">
                                    <div class="fw-bold">$${itemTotal}</div>
                                </div>
                                <div class="col-md-1 text-end">
                                    <button class="btn btn-outline-danger btn-sm" 
                                            onclick="removeItem(${item.id})">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                `);
                
                cartContainer.append(cartItem);
            });
        }
        
        function updateQuantity(cartId, newQuantity) {
            if (newQuantity < 1) {
                removeItem(cartId);
                return;
            }
            
            $.ajax({
                url: `/api/cart/${cartId}`,
                method: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({ quantity: newQuantity }),
                success: function() {
                    loadCartItems();
                    showToast('Cart updated', 'success');
                },
                error: function(xhr) {
                    const response = JSON.parse(xhr.responseText);
                    showToast(response.message, 'error');
                }
            });
        }
        
        function removeItem(cartId) {
            $.ajax({
                url: `/api/cart/${cartId}`,
                method: 'DELETE',
                success: function() {
                    loadCartItems();
                    showToast('Item removed from cart', 'success');
                },
                error: function(xhr) {
                    const response = JSON.parse(xhr.responseText);
                    showToast(response.message, 'error');
                }
            });
        }
        
        function updateCartSummary() {
            let subtotal = 0;
            cartItems.forEach(function(item) {
                subtotal += item.product.price * item.quantity;
            });
            
            $('#subtotal').text('$' + subtotal.toFixed(2));
            $('#total').text('$' + subtotal.toFixed(2));
            
            const totalItems = cartItems.reduce((sum, item) => sum + item.quantity, 0);
            $('#cartCount').text(totalItems);
            if (totalItems > 0) {
                $('#cartCount').show();
            } else {
                $('#cartCount').hide();
            }
        }
        
        function showToast(message, type) {
            const toastClass = type === 'success' ? 'bg-success' : 'bg-danger';
            const toast = $(`
                <div class="toast align-items-center text-white ${toastClass} border-0" role="alert">
                    <div class="d-flex">
                        <div class="toast-body">${message}</div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                    </div>
                </div>
            `);
            
            $('#toastContainer').append(toast);
            const bsToast = new bootstrap.Toast(toast[0]);
            bsToast.show();
            
            toast.on('hidden.bs.toast', function() {
                $(this).remove();
            });
        }
    </script>
</body>
</html>