package principal.servicio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Alumno;
import principal.modelo.Progreso;
import principal.persistencia.AlumnoRepo;
import principal.persistencia.ProgresoRepo;
import principal.servicio.interfaces.AlumnoService;
import principal.servicio.interfaces.ProgresoService;

@Service
public class ProgresoServiceImpl implements ProgresoService {
	
	@Autowired
	private ProgresoRepo progresoRepo;

	@Override
    public Progreso guardarProgreso(Progreso progreso) {
        return progresoRepo.save(progreso);
    }
	
	@Override
	public Optional<Progreso> obtenerProgresoPorId(Integer id){
		return progresoRepo.findById(id);
	}
	
	@Override
	public void eliminarProgresoPorId(Integer id) {
		progresoRepo.delete(progresoRepo.findById(id).get());
		
	}

}
