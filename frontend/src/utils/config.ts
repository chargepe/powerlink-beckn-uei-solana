export const formattedINR = (rate: number) => {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "INR",
  }).format(rate);
};

export function setItem(key: string, value: any) {
  localStorage.setItem(key, JSON.stringify(value));
}

export function getItem(key: string) {
  const value = localStorage.getItem(key);
  if (value) {
    return JSON.parse(value);
  }

  return null;
}

export function removeItem(key: string) {
  localStorage.removeItem(key);
}

export const dateOnly = (dateStr: string) => dateStr.split("T")[0];

//get time from ISO string

// Extract the time (in hours, minutes, and seconds)
export const time = (isoString: string) => {
  const date = new Date(isoString);
  return date.toLocaleTimeString("en-US");
};

export function convertToAMPM(hour:number|string | null) {
  const hourNumber = Number(hour);
  if (isNaN(hourNumber) || hourNumber < 0 || hourNumber > 23) {
    console.error("Invalid hour. Please provide an hour between 0 and 23.");
    return '';
  }

  const ampm = hourNumber >= 12 ? "PM" : "AM";
  const formattedHour = hourNumber % 12 || 12;

  return `${formattedHour} ${ampm}`;
}
