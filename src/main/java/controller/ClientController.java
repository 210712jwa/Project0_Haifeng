package controller;

import java.util.List;

import dto.AddOrEditClientDTO;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import service.ClientService;
import model.Client;
import model.ClientWithAccount;

public class ClientController implements Controller {

private ClientService clientService;
	
	public ClientController() {
		this.clientService = new ClientService();
	}
	
	private Handler getAllClients = (ctx) -> {		
		List<Client> clients = clientService.getAllClients();
		
		ctx.status(200);
		ctx.json(clients);
	};
	
	private Handler getClientByid = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		Client client = clientService.getClientById(clientid);
		ctx.status(200);
		ctx.json(client);
	};
	
	private Handler addClientByid = (ctx) -> {
		AddOrEditClientDTO newClient = ctx.bodyAsClass(AddOrEditClientDTO.class);
		Client client = clientService.addClient(newClient);
		ctx.json(client);
	};

	private Handler editClientByid = (ctx) -> {
		AddOrEditClientDTO editClient = ctx.bodyAsClass(AddOrEditClientDTO.class);
		String clientid = ctx.pathParam(":clientid");
		Client client = clientService.editClient(clientid, editClient);
		ctx.json(client);
	};
	
	private Handler deleteClientByid = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		clientService.deleteClient(clientid);
	};

	private Handler clientWithAccount = (ctx) -> {
		String clientid = ctx.pathParam(":clientid");
		List<ClientWithAccount> clientWithAccounts = clientService.clientWithAccount(clientid);
		ctx.json(clientWithAccounts);	
	};
	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/client", getAllClients);
		app.get("/client/:clientid", getClientByid);
		app.post("/client", addClientByid);
		app.put("/client/:clientid", editClientByid);
		app.delete("/client/:clientid", deleteClientByid);
		app.get("/client/:clientid/accounts", clientWithAccount);
	}
	

}
