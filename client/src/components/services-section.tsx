import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useToast } from "@/hooks/use-toast";
import { apiRequest } from "@/lib/queryClient";

const serviceBookingSchema = z.object({
  serviceType: z.enum(["custom_build", "cleaning", "repair"]),
  description: z.string().min(10, "Please provide more details"),
  customerEmail: z.string().email("Please enter a valid email"),
  customerPhone: z.string().optional(),
});

type ServiceBookingForm = z.infer<typeof serviceBookingSchema>;

export default function ServicesSection() {
  const [selectedService, setSelectedService] = useState<string | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const { toast } = useToast();

  const form = useForm<ServiceBookingForm>({
    resolver: zodResolver(serviceBookingSchema),
    defaultValues: {
      serviceType: "custom_build",
      description: "",
      customerEmail: "",
      customerPhone: "",
    },
  });

  const createBookingMutation = useMutation({
    mutationFn: async (data: ServiceBookingForm) => {
      const response = await apiRequest("POST", "/api/services", data);
      return response.json();
    },
    onSuccess: () => {
      setIsDialogOpen(false);
      form.reset();
      toast({
        title: "Booking Submitted",
        description: "We'll contact you within 24 hours to discuss your service request.",
      });
    },
    onError: (error: any) => {
      toast({
        title: "Error",
        description: error.message || "Failed to submit booking",
        variant: "destructive",
      });
    },
  });

  const onSubmit = (data: ServiceBookingForm) => {
    createBookingMutation.mutate(data);
  };

  const services = [
    {
      id: "custom_build",
      title: "Custom Builds",
      description: "Let our experts build your dream keyboard with your choice of switches, keycaps, and layout.",
      image: "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=200",
      buttonText: "Start Custom Build",
    },
    {
      id: "cleaning",
      title: "Professional Cleaning",
      description: "Deep cleaning service to restore your keyboard to like-new condition with ultrasonic cleaning.",
      image: "https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=200",
      buttonText: "Book Cleaning",
    },
    {
      id: "repair",
      title: "Repair & Modification",
      description: "Expert repair services including switch replacement, PCB fixes, and custom modifications.",
      image: "https://images.unsplash.com/photo-1541140532154-b024d705b90a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=300&h=200",
      buttonText: "Request Repair",
    },
  ];

  const handleServiceSelect = (serviceId: string) => {
    setSelectedService(serviceId);
    form.setValue("serviceType", serviceId as "custom_build" | "cleaning" | "repair");
    setIsDialogOpen(true);
  };

  return (
    <section className="py-16 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center mb-12">
          <h3 className="text-3xl font-bold mb-4">Professional Services</h3>
          <p className="text-gray-600 max-w-2xl mx-auto">
            From custom builds to professional cleaning and repairs, our expert team provides comprehensive keyboard services.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8">
          {services.map((service) => (
            <Card key={service.id} className="text-center p-8 border border-gray-200 hover:shadow-lg transition-shadow">
              <CardContent className="p-0">
                <img 
                  src={service.image}
                  alt={service.title}
                  className="w-full h-48 object-cover rounded-lg mb-6"
                />
                <h4 className="text-xl font-semibold mb-4">{service.title}</h4>
                <p className="text-gray-600 mb-6">{service.description}</p>
                <Button 
                  className="bg-secondary hover:bg-blue-600 text-white"
                  onClick={() => handleServiceSelect(service.id)}
                >
                  {service.buttonText}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogContent className="max-w-md">
            <DialogHeader>
              <DialogTitle>
                {selectedService === "custom_build" && "Custom Build Service"}
                {selectedService === "cleaning" && "Cleaning Service"}
                {selectedService === "repair" && "Repair Service"}
              </DialogTitle>
            </DialogHeader>
            <Form {...form}>
              <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="customerEmail"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email Address</FormLabel>
                      <FormControl>
                        <Input {...field} type="email" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="customerPhone"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Phone Number (Optional)</FormLabel>
                      <FormControl>
                        <Input {...field} type="tel" />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="description"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Service Details</FormLabel>
                      <FormControl>
                        <Textarea 
                          {...field} 
                          placeholder="Please describe what you need..."
                          rows={4}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="flex justify-end space-x-2">
                  <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                    Cancel
                  </Button>
                  <Button 
                    type="submit" 
                    disabled={createBookingMutation.isPending}
                  >
                    Submit Request
                  </Button>
                </div>
              </form>
            </Form>
          </DialogContent>
        </Dialog>
      </div>
    </section>
  );
}
