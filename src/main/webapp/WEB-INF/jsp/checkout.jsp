<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Checkout - KeyCraft</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft</a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="/products">Products</a>
            <a class="nav-link" href="/cart"><i class="fas fa-shopping-cart"></i> Cart</a>
            <a class="nav-link" href="/orders">Orders</a>
            <a class="nav-link" href="/logout">Logout</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-8">
            <h2><i class="fas fa-credit-card"></i> Checkout</h2>

            <!-- Billing Information -->
            <div class="card">
                <div class="card-header">
                    <h5>Billing Information</h5>
                </div>
                <div class="card-body">
                    <form id="checkout-form">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="firstName" class="form-label">First Name</label>
                                <input type="text" class="form-control" id="firstName"
                                       value="${not empty user ? user.firstName : ''}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="lastName" class="form-label">Last Name</label>
                                <input type="text" class="form-control" id="lastName"
                                       value="${not empty user ? user.lastName : ''}" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email"
                                   value="${not empty user ? user.email : ''}" required>
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" placeholder="123 Main St" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="city" class="form-label">City</label>
                                <input type="text" class="form-control" id="city" required>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="state" class="form-label">State</label>
                                <select class="form-control" id="state" required>
                                    <option value="">Select State</option>
                                    <option value="CA">California</option>
                                    <option value="NY">New York</option>
                                    <option value="TX">Texas</option>
                                </select>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="zip" class="form-label">ZIP Code</label>
                                <input type="text" class="form-control" id="zip" required>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Payment Information -->
            <div class="card mt-4">
                <div class="card-header"><h5>Payment Information</h5></div>
                <div class="card-body">
                    <div class="alert alert-info"><i class="fas fa-info-circle"></i> This is a demo. No real payment.</div>
                    <form id="payment-form">
                        <div class="mb-3">
                            <label for="cardNumber" class="form-label">Card Number</label>
                            <input type="text" class="form-control" id="cardNumber" placeholder="1234 5678 9012 3456" maxlength="19">
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="expiryMonth" class="form-label">Expiry Month</label>
                                <select class="form-control" id="expiryMonth">
                                    <option value="">Month</option>
                                    <c:forEach begin="1" end="12" var="month">
                                        <option value="${month}">${month}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="expiryYear" class="form-label">Expiry Year</label>
                                <select class="form-control" id="expiryYear">
                                    <option value="">Year</option>
                                    <c:forEach begin="2024" end="2034" var="year">
                                        <option value="${year}">${year}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="cvv" class="form-label">CVV</label>
                                <input type="text" class="form-control" id="cvv" maxlength="4" placeholder="123">
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Order Summary -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header"><h5>Order Summary</h5></div>
                <div class="card-body">
                    <div id="order-summary"><!-- Loaded by JS --></div>
                    <div class="d-grid">
                        <button class="btn btn-success btn-lg" onclick="processOrder()">
                            <i class="fas fa-lock"></i> Place Order
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Loading Modal -->
<div class="modal fade" id="loadingModal" tabindex="-1" data-bs-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-body text-center">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-3">Processing your order...</p>
            </div>
        </div>
    </div>
</div>

<!-- Toast Notification -->
<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="notification-toast" class="toast" role="alert">
        <div class="toast-header">
            <strong class="me-auto">KeyCraft</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
        </div>
        <div class="toast-body" id="toast-message"></div>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        loadOrderSummary();
        formatCardNumber();
    });

    function loadOrderSummary() {
        fetch('/cart/count')
            .then(res => res.json())
            .then(data => {
                if (data.cartItemCount === 0) {
                    window.location.href = '/cart';
                }
            });
    }

    function formatCardNumber() {
        const cardNumberInput = document.getElementById('cardNumber');
        cardNumberInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
            e.target.value = value.match(/.{1,4}/g)?.join(' ') || value;
        });
    }

    function processOrder() {
        const requiredFields = ['firstName', 'lastName', 'email', 'address', 'city', 'state', 'zip'];
        let isValid = true;
        const formData = {};

        requiredFields.forEach(id => {
            const field = document.getElementById(id);
            if (!field.value.trim()) {
                field.classList.add('is-invalid');
                isValid = false;
            } else {
                field.classList.remove('is-invalid');
                formData[id] = field.value.trim();
            }
        });

        if (!isValid) {
            showNotification('Please fill in all required fields', 'error');
            return;
        }

        const loadingModal = new bootstrap.Modal(document.getElementById('loadingModal'));
        loadingModal.show();

        fetch('/checkout/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(formData)
        })
        .then(res => res.json())
        .then(data => {
            loadingModal.hide();
            if (data.success) {
                showNotification(data.message, 'success');
                setTimeout(() => {
                    window.location.href = data.redirectUrl || '/checkout/success?orderId=' + data.orderId;
                }, 1500);
            } else {
                showNotification(data.message, 'error');
            }
        })
        .catch(() => {
            loadingModal.hide();
            showNotification('Error processing order', 'error');
        });
    }

    function showNotification(msg, type) {
        const toast = document.getElementById('notification-toast');
        const toastMsg = document.getElementById('toast-message');
        toastMsg.textContent = msg;
        toast.className = `toast ${type == 'success' ? 'bg-success text-white' : 'bg-danger text-white'}`;
        new bootstrap.Toast(toast).show();
    }
</script>
</body>
</html>
