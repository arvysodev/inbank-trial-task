export type LoanDecisionRequest = {
  personalCode: string;
  loanAmount: number;
  loanPeriodMonths: number;
};

export type LoanDecisionResponse = {
  decision: string;
  approvedAmount: number | null;
  approvedPeriodMonths: number | null;
  requestedAmount: number;
  requestedPeriodMonths: number;
};

export type ApiProblem = {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
};
