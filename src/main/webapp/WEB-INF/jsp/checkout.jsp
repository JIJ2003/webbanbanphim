<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - KeyCraft</title>
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
        </div>
    </nav>

    <!-- Checkout Content -->
    <div class="container my-4">
        <div class="row">
            <div class="col-lg-8">
                <h2><i class="fas fa-credit-card"></i> Checkout</h2>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="/checkout/process" method="post">
                    <!-- Shipping Information -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5><i class="fas fa-truck"></i> Shipping Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">First Name</label>
                                    <input type="text" class="form-control" value="${user.firstName}" readonly>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Last Name</label>
                                    <input type="text" class="form-control" value="${user.lastName}" readonly>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" value="${user.email}" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Shipping Address *</label>
                                <textarea class="form-control" name="shippingAddress" rows="3" required 
                                          placeholder="Enter your complete shipping address"></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Billing Information -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5><i class="fas fa-file-invoice"></i> Billing Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" id="sameAsShipping" checked>
                                <label class="form-check-label" for="sameAsShipping">
                                    Same as shipping address
                                </label>
                            </div>
                            <div class="mb-3" id="billingAddressGroup" style="display: none;">
                                <label class="form-label">Billing Address *</label>
                                <textarea class="form-control" name="billingAddress" rows="3" 
                                          placeholder="Enter your billing address"></textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Payment Information -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5><i class="fas fa-credit-card"></i> Payment Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label class="form-label">Payment Method *</label>
                                <select class="form-select" name="paymentMethod" required>
                                    <option value="">Select payment method</option>
                                    <option value="Credit Card">Credit Card</option>
                                    <option value="Debit Card">Debit Card</option>
                                    <option value="PayPal">PayPal</option>
                                </select>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Cardholder Name *</label>
                                    <input type="text" class="form-control" name="cardholderName" required 
                                           value="${user.firstName} ${user.lastName}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Card Number *</label>
                                    <input type="text" class="form-control" name="cardNumber" required 
                                           placeholder="1234 5678 9012 3456" maxlength="19">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Expiry Date *</label>
                                    <input type="text" class="form-control" name="expiryDate" required 
                                           placeholder="MM/YY" maxlength="5">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">CVV *</label>
                                    <input type="text" class="form-control" name="cvv" required 
                                           placeholder="123" maxlength="4">
                                </div>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-success btn-lg w-100">
                        <i class="fas fa-lock"></i> Place Order
                    </button>
                </form>
            </div>
            
            <!-- Order Summary -->
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <h5><i class="fas fa-receipt"></i> Order Summary</h5>
                    </div>
                    <div class="card-body">
                        <c:forEach items="${cartItems}" var="item">
                            <div class="d-flex justify-content-between mb-2">
                                <div>
                                    <div class="fw-bold">${item.product.name}</div>
                                    <small class="text-muted">Qty: ${item.quantity} × $${item.product.price}</small>
                                </div>
                                <div class="fw-bold">
                                    $${item.product.price * item.quantity}
                                </div>
                            </div>
                        </c:forEach>
                        <hr>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Subtotal:</span>
                            <span>$${total}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Shipping:</span>
                            <span class="text-success">FREE</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between">
                            <strong>Total:</strong>
                            <strong>$${total}</strong>
                        </div>
                    </div>
                </div>
                
                <div class="mt-3">
                    <div class="text-center">
                        <i class="fas fa-shield-alt text-success"></i>
                        <small class="text-muted d-block">Secure SSL encrypted payment</small>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            // Handle same as shipping address checkbox
            $('#sameAsShipping').change(function() {
                if (this.checked) {
                    $('#billingAddressGroup').hide();
                    $('textarea[name="billingAddress"]').removeAttr('required');
                } else {
                    $('#billingAddressGroup').show();
                    $('textarea[name="billingAddress"]').attr('required', 'required');
                }
            });

            // Copy shipping address to billing if same as shipping is checked
            $('form').submit(function(e) {
                if ($('#sameAsShipping').is(':checked')) {
                    const shippingAddress = $('textarea[name="shippingAddress"]').val();
                    $('textarea[name="billingAddress"]').val(shippingAddress);
                }
            });

            // Format card number
            $('input[name="cardNumber"]').on('input', function() {
                let value = this.value.replace(/\D/g, '');
                value = value.replace(/(\d{4})(?=\d)/g, '$1 ');
                this.value = value;
            });

            // Format expiry date
            $('input[name="expiryDate"]').on('input', function() {
                let value = this.value.replace(/\D/g, '');
                if (value.length >= 2) {
                    value = value.substring(0, 2) + '/' + value.substring(2, 4);
                }
                this.value = value;
            });

            // Allow only numbers for CVV
            $('input[name="cvv"]').on('input', function() {
                this.value = this.value.replace(/\D/g, '');
            });
        });
    </script>
</body>
</html>