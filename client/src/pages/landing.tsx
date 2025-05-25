import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

export default function Landing() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-primary">KeyCraft</h1>
            </div>
            <nav className="hidden md:flex space-x-8">
              <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">Keyboards</a>
              <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">Accessories</a>
              <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">Custom Builds</a>
              <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">Services</a>
            </nav>
            <div className="flex items-center space-x-4">
              <Button asChild>
                <a href="/api/login">Sign In</a>
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-primary text-white py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-4xl md:text-5xl font-bold mb-6">Premium Mechanical Keyboards</h2>
              <p className="text-xl text-gray-300 mb-8">
                Discover the perfect typing experience with our curated collection of mechanical keyboards, 
                custom builds, and professional services.
              </p>
              <div className="flex flex-col sm:flex-row gap-4">
                <Button size="lg" className="bg-accent hover:bg-yellow-500 text-white">
                  <a href="/api/login">Shop Now</a>
                </Button>
                <Button size="lg" variant="outline" className="border-gray-300 hover:bg-white hover:text-primary text-white">
                  <a href="/api/login">Custom Build</a>
                </Button>
              </div>
            </div>
            <div className="relative">
              <img 
                src="https://images.unsplash.com/photo-1541140532154-b024d705b90a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&h=600" 
                alt="Premium mechanical keyboard with RGB lighting" 
                className="rounded-xl shadow-2xl w-full h-auto"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h3 className="text-3xl font-bold mb-4">Why Choose KeyCraft?</h3>
            <p className="text-gray-600 max-w-2xl mx-auto">
              We're passionate about providing the best mechanical keyboard experience with premium products and expert services.
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            <Card className="text-center p-8 hover:shadow-lg transition-shadow">
              <CardContent className="pt-6">
                <div className="w-16 h-16 bg-accent/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="fas fa-keyboard text-2xl text-accent"></i>
                </div>
                <h4 className="text-xl font-semibold mb-4">Premium Quality</h4>
                <p className="text-gray-600">
                  Carefully curated selection of the finest mechanical keyboards from top brands worldwide.
                </p>
              </CardContent>
            </Card>

            <Card className="text-center p-8 hover:shadow-lg transition-shadow">
              <CardContent className="pt-6">
                <div className="w-16 h-16 bg-secondary/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="fas fa-tools text-2xl text-secondary"></i>
                </div>
                <h4 className="text-xl font-semibold mb-4">Expert Services</h4>
                <p className="text-gray-600">
                  Professional custom builds, cleaning, and repair services by certified technicians.
                </p>
              </CardContent>
            </Card>

            <Card className="text-center p-8 hover:shadow-lg transition-shadow">
              <CardContent className="pt-6">
                <div className="w-16 h-16 bg-success/10 rounded-full flex items-center justify-center mx-auto mb-4">
                  <i className="fas fa-shipping-fast text-2xl text-success"></i>
                </div>
                <h4 className="text-xl font-semibold mb-4">Fast Shipping</h4>
                <p className="text-gray-600">
                  Quick and secure delivery worldwide with full insurance and tracking.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Categories Preview */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h3 className="text-3xl font-bold mb-4">Shop by Category</h3>
            <p className="text-gray-600">Find the perfect keyboard for your needs</p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            <div className="text-center group cursor-pointer">
              <div className="relative overflow-hidden rounded-lg mb-4">
                <img 
                  src="https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                  alt="Compact mechanical keyboard" 
                  className="w-full h-48 object-cover group-hover:scale-105 transition-transform"
                />
                <Badge className="absolute top-2 right-2 bg-accent">Popular</Badge>
              </div>
              <h4 className="font-semibold">Compact Keyboards</h4>
            </div>

            <div className="text-center group cursor-pointer">
              <div className="relative overflow-hidden rounded-lg mb-4">
                <img 
                  src="https://images.unsplash.com/photo-1595044426077-d36d9236d54a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                  alt="Full-size mechanical keyboard" 
                  className="w-full h-48 object-cover group-hover:scale-105 transition-transform"
                />
              </div>
              <h4 className="font-semibold">Full Size</h4>
            </div>

            <div className="text-center group cursor-pointer">
              <div className="relative overflow-hidden rounded-lg mb-4">
                <img 
                  src="https://images.unsplash.com/photo-1551698618-1dfe5d97d256?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                  alt="Colorful mechanical keyboard keycaps" 
                  className="w-full h-48 object-cover group-hover:scale-105 transition-transform"
                />
              </div>
              <h4 className="font-semibold">Keycaps & Switches</h4>
            </div>

            <div className="text-center group cursor-pointer">
              <div className="relative overflow-hidden rounded-lg mb-4">
                <img 
                  src="https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                  alt="Custom keyboard building components and tools" 
                  className="w-full h-48 object-cover group-hover:scale-105 transition-transform"
                />
                <Badge className="absolute top-2 right-2 bg-secondary">New</Badge>
              </div>
              <h4 className="font-semibold">Custom Builds</h4>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-primary">
        <div className="max-w-4xl mx-auto text-center px-4 sm:px-6 lg:px-8">
          <h3 className="text-3xl font-bold text-white mb-4">Ready to Find Your Perfect Keyboard?</h3>
          <p className="text-xl text-gray-300 mb-8">
            Join thousands of satisfied customers who've found their ideal typing experience with KeyCraft.
          </p>
          <Button size="lg" className="bg-accent hover:bg-yellow-500 text-white">
            <a href="/api/login">Get Started Today</a>
          </Button>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-primary text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <h4 className="text-xl font-bold mb-4">KeyCraft</h4>
              <p className="text-gray-300 mb-4">Your premium destination for mechanical keyboards and accessories.</p>
              <div className="flex space-x-4">
                <a href="#" className="text-gray-300 hover:text-white transition-colors">
                  <i className="fab fa-facebook text-xl"></i>
                </a>
                <a href="#" className="text-gray-300 hover:text-white transition-colors">
                  <i className="fab fa-instagram text-xl"></i>
                </a>
                <a href="#" className="text-gray-300 hover:text-white transition-colors">
                  <i className="fab fa-twitter text-xl"></i>
                </a>
              </div>
            </div>

            <div>
              <h5 className="font-semibold mb-4">Shop</h5>
              <ul className="space-y-2 text-gray-300">
                <li><a href="#" className="hover:text-white transition-colors">Mechanical Keyboards</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Keycaps</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Switches</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Accessories</a></li>
              </ul>
            </div>

            <div>
              <h5 className="font-semibold mb-4">Services</h5>
              <ul className="space-y-2 text-gray-300">
                <li><a href="#" className="hover:text-white transition-colors">Custom Builds</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Professional Cleaning</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Repairs</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Modifications</a></li>
              </ul>
            </div>

            <div>
              <h5 className="font-semibold mb-4">Support</h5>
              <ul className="space-y-2 text-gray-300">
                <li><a href="#" className="hover:text-white transition-colors">Contact Us</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Shipping Info</a></li>
                <li><a href="#" className="hover:text-white transition-colors">Returns</a></li>
                <li><a href="#" className="hover:text-white transition-colors">FAQ</a></li>
              </ul>
            </div>
          </div>

          <div className="border-t border-gray-600 mt-8 pt-8 text-center text-gray-300">
            <p>&copy; 2024 KeyCraft. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
