package br.com.fiap.taykarus.motors.infrastructure.config;

import br.com.fiap.taykarus.motors.application.port.in.ManageVehicleUseCase;
import br.com.fiap.taykarus.motors.application.port.in.SaleUseCase;
import br.com.fiap.taykarus.motors.application.port.out.SaleRepository;
import br.com.fiap.taykarus.motors.application.port.out.VehicleRepository;
import br.com.fiap.taykarus.motors.application.service.SaleService;
import br.com.fiap.taykarus.motors.application.service.VehicleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public ManageVehicleUseCase manageVehicleUseCase(VehicleRepository vehicleRepository) {
        return new VehicleService(vehicleRepository);
    }

    @Bean
    public SaleUseCase saleUseCase(VehicleRepository vehicleRepository, SaleRepository saleRepository) {
        return new SaleService(vehicleRepository, saleRepository);
    }
}