package com.pms.in.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pms.in.entities.PensionerDetails;
import com.pms.in.exception.AdminDoesNotExistsException;
import com.pms.in.exception.PensionerAlreadyExistsException;
import com.pms.in.exception.PensionerDetailsNotFoundException;
import com.pms.in.repository.PensionerRepository;

@Service
public class PensionerService implements IPensionerService {
	public static final Logger LOG = LoggerFactory.getLogger(PensionerService.class);

	@Autowired
	private AdminService admin;

	@Autowired
	private AbstractUserService abst;

	@Autowired
	private PensionerRepository pensionerRepository;

	public boolean validatePensioner(String name, int pan, int aadhar, String type) {

		LOG.info("ServicevalidatePensioner");
		PensionerDetails validatepensioner = null;

		validatepensioner = pensionerRepository.findByAadhar(aadhar);
		if (admin.getIsLoggedIn() || abst.getIsLoggedIn()) {
			if (validatepensioner != null) {
				if (validatepensioner.getPan() == pan && validatepensioner.getPensionType().equalsIgnoreCase(type)) {
					LOG.info("Matched");
					return true;
				} else {
					LOG.info("Invalid pensioner detail provided, please provide valid detail.");
					throw new PensionerDetailsNotFoundException(
							"Invalid pensioner detail provided, please provide valid detail.");

				}
			} else {
				LOG.info("Invalid pensioner detail provided, please provide valid detail.");
				throw new PensionerDetailsNotFoundException(
						"Invalid pensioner detail provided, please provide valid detail.");

			}
		} else {
			LOG.info("User have to Login first");
			throw new AdminDoesNotExistsException();
		}

	}

	@Override
	public PensionerDetails addPensionerDetails(PensionerDetails pensionerdetails) {
		LOG.info("ServiceaddPensionerDetails");
		if (admin.getIsLoggedIn() || abst.getIsLoggedIn()) {
			if (!pensionerRepository.existsById(pensionerdetails.getPensioner_id()))
				return pensionerRepository.save(pensionerdetails);
			else {
				LOG.info(pensionerdetails.getPensioner_id() + " already exists.");
				throw new PensionerAlreadyExistsException(
						pensionerdetails.getPensioner_id() + " This Pensioner  ALready Exits.");
			}
		} else {
			LOG.info("User have to Login first");
			throw new AdminDoesNotExistsException();
		}
	}

	@Override
	public PensionerDetails updatePensionerDetails(PensionerDetails pensionerdetails) {
		LOG.info("ServiceUpadtePensionerDetails");
		if (admin.getIsLoggedIn() || abst.getIsLoggedIn()) {
			if (pensionerRepository.existsById(pensionerdetails.getPensioner_id()))
				return pensionerRepository.save(pensionerdetails);

			else {
				LOG.info("Pensioner is NOT available.");
				throw new PensionerDetailsNotFoundException(" This Pensioner Details is not found.");
			}
		} else {
			LOG.info("User have to Login first");
			throw new AdminDoesNotExistsException();
		}
	}

	public PensionerDetails deletePensionerDetails(int pensioner_id) {
		LOG.info("ServiceDeletePensionerDetails");
		if (admin.getIsLoggedIn() || abst.getIsLoggedIn()) {
			if (pensionerRepository.existsById(pensioner_id)) {
				pensionerRepository.deleteById(pensioner_id);
				return null;
			} else {
				LOG.info("Pensioner is NOT available.");
				throw new PensionerDetailsNotFoundException(" Pensioner Details is not found.");
			}
		} else {
			LOG.info("User have to Login first");
			throw new AdminDoesNotExistsException();
		}
	}

	public List<PensionerDetails> getAllPensionersDetails() {
		LOG.info("ServicegetAllPensioner");
		if (admin.getIsLoggedIn() || abst.getIsLoggedIn()) {
			return pensionerRepository.findAll();
		} else {
			LOG.info("User have to Login first");
			throw new AdminDoesNotExistsException();
		}
	}
}