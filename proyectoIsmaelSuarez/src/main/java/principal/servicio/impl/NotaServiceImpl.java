package principal.servicio.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import principal.modelo.Alumno;
import principal.modelo.Nota;
import principal.persistencia.AlumnoRepo;
import principal.persistencia.NotaRepo;
import principal.servicio.interfaces.AlumnoService;
import principal.servicio.interfaces.NotaService;

@Service
public class NotaServiceImpl implements NotaService {
	
	@Autowired
	private NotaRepo notaRepo;

	@Override
    public Nota guardarNota(Nota nota) {
        return notaRepo.save(nota);
    }
	
	@Override
	public Optional<Nota> obtenerNotaPorId(Integer id){
		return notaRepo.findById(id);
	}
	
	@Override
	public void eliminarNotaPorId(Integer id) {
		notaRepo.delete(notaRepo.findById(id).get());
		
	}

}
