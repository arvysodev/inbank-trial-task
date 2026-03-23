import type {
  ApiProblem,
  LoanDecisionRequest,
  LoanDecisionResponse,
} from "../types/loan";

export async function calculateLoanDecision(
  request: LoanDecisionRequest,
): Promise<LoanDecisionResponse> {
  const response = await fetch("/api/loan/decision", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    let problem: ApiProblem | null = null;

    try {
      problem = (await response.json()) as ApiProblem;
    } catch {
      problem = null;
    }

    throw new Error(problem?.detail || "Failed to calculate loan decision.");
  }

  return (await response.json()) as LoanDecisionResponse;
}
