package com.rts.tap.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.rts.tap.model.SubRequirements;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

	private JavaMailSender mailSender;

	public EmailUtil(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	public void sendApprovalNotificationEmail(String partnerEmail, String clientName, String organizationName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(partnerEmail);
		message.setSubject("Client Approval Notification");
		message.setText("Dear Valued Client Partner,\n\n" + "We are pleased to inform you that the client " + clientName
				+ " from " + organizationName + " has been successfully approved.\n"
				+ "Their access credentials have been sent to them.\n\n"
				+ "If you have any questions, feel free to reach out.\n\n" + "Thank you for your partnership.\n"
				+ "Best regards,\n" + "RelevantZ Technology Services");

		mailSender.send(message);
		System.out.println("Approval notification email sent to: " + partnerEmail);
	}

	public void sendRejectionNotificationEmail(String partnerEmail, String clientName, String organizationName,
			String reason) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(partnerEmail);
		message.setSubject("Client Rejection Notification");
		message.setText("Dear Valued Client Partner,\n\n" + "We regret to inform you that the client " + clientName
				+ " from " + organizationName + " has been rejected.\n" + "Reason for rejection: " + reason + "\n\n"
				+ "We appreciate your understanding in this matter.\n"
				+ "For any further inquiries, please do not hesitate to contact us.\n\n"
				+ "Thank you for your cooperation.\n" + "Best regards,\n" + "RelevantZ Technology Services");

		mailSender.send(message);
		System.out.println("Rejection notification email sent to: " + partnerEmail);
	}

	public void sendApprovalEmail(String email, String password, String organizationName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your Account Has Been Approved");
		message.setText("Dear Client,\n\n" + "Congratulations! Your account with " + organizationName
				+ " has been approved.\n" + "Below are your login credentials:\n" + "Email: " + email + "\n"
				+ "Password: " + password + "\n\n"
				+ "We look forward to serving you. If you have any questions, please feel free to contact us.\n\n"
				+ "Best regards,\n" + "RelevantZ Technology Services");

		mailSender.send(message);
		System.out.println("Approval email sent to: " + email);
	}

	public void sendBuApprovalRequestNotificationEmail(String buhEmail, String buhName, String clientName,
			String organizationName, String mrfClientPartnerName, String mrfClientPartnerRole,
			String mrfClientPartnerEmail, String mrfClientPartnerMobile, byte[] mrfTemplateData) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setTo(buhEmail);
			helper.setSubject("MRF Approval Request");

			String emailBody = "Dear Business Unit Head, " + buhName + ",\n\n" + "I hope this email finds you well.\n\n"
					+ "This is to inform you that a new request for approval has been initiated for the client "
					+ clientName + " from the organization " + organizationName + ".\n\n "
					+ "I kindly ask you to review the details at your earliest convenience and take the appropriate action required for this request.\n\n"
					+ "Thank you for your attention to this important matter.\n\n"
					+ "--------------------------------------------\n\n" + "Thanks & Regards,\n\n"
					+ mrfClientPartnerName.toUpperCase() + "\n" + mrfClientPartnerRole.toLowerCase() + "\n" + "Email: "
					+ mrfClientPartnerEmail + "\n" + "Location: " + mrfClientPartnerMobile + "\n";

			helper.setText(emailBody, false);

			if (mrfTemplateData != null) {
				@SuppressWarnings("unused")
				InputStream is = new ByteArrayInputStream(mrfTemplateData);
				helper.addAttachment("MRF_Template.pdf", new InputStreamSource() {
					@Override
					public InputStream getInputStream() {
						return new ByteArrayInputStream(mrfTemplateData);
					}
				});
			}

			mailSender.send(mimeMessage);
			System.out.println("BU approval request notification email with attachment sent to: " + buhEmail);
		} catch (Exception e) {
			System.err.println("Failed to send BU approval request notification email to: " + buhEmail);
			e.printStackTrace();
		}
	}

	// team B
	public void sendHtmlEmail(String clientEmail, String subject, String emailBody) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

		try {
			messageHelper.setTo(clientEmail);
			messageHelper.setSubject(subject);
			messageHelper.setText(emailBody, true); // true indicates html content

			mailSender.send(mimeMessage);
			System.out.println("Renegotiation email sent to: " + clientEmail);
		} catch (MessagingException e) {
			e.printStackTrace(); // handle exception as needed
		}
	}

	public String createRenegotiationEmail(String clientEmail, String clientName, String organizationName,
			double budget, List<SubRequirements> allSubRequirements, HashMap<Long, Double> suggestedBudget) {

		StringBuilder subRequirementTable = new StringBuilder();
		subRequirementTable.append(
				"<table style='width: 100%; border: 1px solid #ddd; border-collapse: collapse; text-align: left;'>")
				.append("<tr style='background-color: #4CAF50; color: white;'>")
				.append("<th style='padding: 12px; width: 10%;'>S.No</th>")
				.append("<th style='padding: 12px; width: 30%;'>Role</th>")
				.append("<th style='padding: 12px; width: 20%;'>Resource Count</th>")
				.append("<th style='padding: 12px; width: 40%;'>Suggested Budget</th>") // Increased width for budget
																						// column
				.append("</tr>");

		double totalSuggestedBudget = 0.0; // For calculating total suggested budget
		final double conversionRate = 0.012; // Assume conversion rate: 1 INR = 0.012 USD

		// Locale for Indian number formatting
		NumberFormat rupeeFormat = NumberFormat.getInstance(new Locale("en", "IN"));
		rupeeFormat.setGroupingUsed(true); // Enable grouping for thousands
		rupeeFormat.setMinimumFractionDigits(0); // No decimal places
		rupeeFormat.setMaximumFractionDigits(0); // No decimal places

		// Check if there are sub-requirements and populate the table
		if (allSubRequirements != null && !allSubRequirements.isEmpty()) {
			for (int i = 0; i < allSubRequirements.size(); i++) {
				SubRequirements subReq = allSubRequirements.get(i);
				Long subRequirementId = subReq.getSubRequirementId(); // Assuming this method exists
				Double budgetForResource = suggestedBudget.get(subRequirementId);

				// Format the amounts using the Indian Rupee format
				String suggestedBudgetFormatted = budgetForResource != null
						? "₹" + rupeeFormat.format(budgetForResource)
						: "N/A";
				String budgetInUSDFormatted = budgetForResource != null
						? String.format("$%.2f", budgetForResource * conversionRate)
						: "N/A";

				subRequirementTable.append("<tr>")
						.append("<td style='border: 1px solid #ddd; padding: 12px; text-align: center;'>").append(i + 1)
						.append("</td>").append("<td style='border: 1px solid #ddd; padding: 12px;'>")
						.append(subReq.getRole()).append("</td>")
						.append("<td style='border: 1px solid #ddd; padding: 12px; text-align: center;'>")
						.append(subReq.getResourceCount()).append("</td>")
						.append("<td style='border: 1px solid #ddd; padding: 12px; text-align: center;'>")
						.append(budgetForResource != null ? suggestedBudgetFormatted + " (" + budgetInUSDFormatted + ")"
								: "N/A")
						.append("</td>").append("</tr>");

				// Add to total suggested budget if it exists
				if (budgetForResource != null) {
					totalSuggestedBudget += budgetForResource;
				}
			}
		} else {
			subRequirementTable.append(
					"<tr><td colspan='4' style='border: 1px solid #ddd; padding: 12px; text-align: center;'>No sub-requirements found for this requirement ID.</td></tr>");
		}

		subRequirementTable.append("</table>");

		// Add a footer row for the total budget
		double totalSuggestedBudgetInUSD = totalSuggestedBudget * conversionRate; // Convert total to USD
		String totalSuggestedBudgetFormatted = "₹" + rupeeFormat.format(totalSuggestedBudget); // Format without
																								// decimals
		String totalSuggestedBudgetInUSDFormatted = String.format("$%.2f", totalSuggestedBudgetInUSD);

		subRequirementTable.append("<h5>Total Suggested Budget for Resources: <span style='color: #D74D30;'>"
				+ totalSuggestedBudgetFormatted + " (" + totalSuggestedBudgetInUSDFormatted + ")</span></h5>");

		// Compose the email body
		String emailBody = "<html><body style='font-family: Arial, sans-serif; padding: 20px;'>"
				+ "<h4 style='margin-bottom: 5px;'>Dear " + clientName + ",</h4>"
				+ "<p style='margin-top: 0;'>We hope this message finds you well.</p>"
				+ "<p>Regarding the project with <strong>" + organizationName
				+ "</strong>, we would like to discuss the current budget:</p>"
				+ "<h4 style='color: #D74D30;'>Current Budget: <span style='font-weight: normal;'>" + "$" + budget
				+ "</span></h4>" + "<h5 style='color: #D74D30;'>Reason for Budget Review:</h5>"
				+ "<p>After reviewing the project requirements and associated costs, we find that the budget allocated is insufficient for the successful completion of the project. We recommend a reassessment of the budget to meet the necessary standards and requirements.</p>"
				+ "<h5>Here are the details of the relevant sub-requirements:</h5>" + subRequirementTable.toString()
				+ "<p>We kindly ask you to review and discuss this matter at your earliest convenience.</p>"
				+ "<p>Your understanding and cooperation in this matter are highly appreciated.</p>"
				+ "<p>Thank you!</p>" + "<p>Best regards,<br/>RelevantZ Technology Services</p>" + "</body></html>";

		return emailBody;
	}

}
