package com.iceq.springdata.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iceq.test.services.FitkinTestService;

/**
 * Runnable Test class.
 * 
 * As the @Autowired annotation doesn't work in static context,
 * we're using context.getBean(...) here instead.
 *
 * @author Axel Morgner
 */
public class Application {
	
	public static void main(final String[] args) throws Exception {

		//final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		final FitkinTestService controller = ctx.getBean(FitkinTestService.class);//TestService.class);
		controller.runTest();
		//controller.listCustomers();
		//controller.listCategories();
		//controller.listSuppliers();
		//controller.listOrders();
	
		ctx.close();
		//System.out.println(ctx.getBean(ProductRepository.class).findByName("Chang").productName);
		//System.out.println(ctx.getBean(ProductRepository.class).findByName(".*hang.*").productName);

	}
}
