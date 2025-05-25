import Header from "@/components/header";
import HeroSection from "@/components/hero-section";
import ProductGrid from "@/components/product-grid";
import ServicesSection from "@/components/services-section";
import ShoppingCart from "@/components/shopping-cart";
import { Toaster } from "@/components/ui/toaster";

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <HeroSection />
      
      {/* Product Categories */}
      <section className="py-12 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h3 className="text-2xl font-bold text-center mb-8">Shop by Category</h3>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            <div className="text-center group cursor-pointer">
              <img 
                src="https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                alt="Compact mechanical keyboard" 
                className="w-full h-48 object-cover rounded-lg group-hover:shadow-lg transition-shadow"
              />
              <h4 className="font-semibold mt-4">Compact Keyboards</h4>
            </div>
            <div className="text-center group cursor-pointer">
              <img 
                src="https://images.unsplash.com/photo-1595044426077-d36d9236d54a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                alt="Full-size mechanical keyboard" 
                className="w-full h-48 object-cover rounded-lg group-hover:shadow-lg transition-shadow"
              />
              <h4 className="font-semibold mt-4">Full Size</h4>
            </div>
            <div className="text-center group cursor-pointer">
              <img 
                src="https://images.unsplash.com/photo-1551698618-1dfe5d97d256?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                alt="Colorful mechanical keyboard keycaps" 
                className="w-full h-48 object-cover rounded-lg group-hover:shadow-lg transition-shadow"
              />
              <h4 className="font-semibold mt-4">Keycaps & Switches</h4>
            </div>
            <div className="text-center group cursor-pointer">
              <img 
                src="https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=300" 
                alt="Custom keyboard building components and tools" 
                className="w-full h-48 object-cover rounded-lg group-hover:shadow-lg transition-shadow"
              />
              <h4 className="font-semibold mt-4">Custom Builds</h4>
            </div>
          </div>
        </div>
      </section>

      <ProductGrid />
      <ServicesSection />
      <ShoppingCart />

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
                <a href="#" className="text-gray-300 hover:text-white transition-colors">
                  <i className="fab fa-youtube text-xl"></i>
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

      <Toaster />
    </div>
  );
}
