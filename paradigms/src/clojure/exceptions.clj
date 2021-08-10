(defn exception [Message]
  (fn [char tail] (format char tail)))

(defn expected-end-of-line [char] (str "Expected end of line, found: " char))
(defn unexpected-char [char pos] (str "Unexpected char: '" char "' at: " pos))
